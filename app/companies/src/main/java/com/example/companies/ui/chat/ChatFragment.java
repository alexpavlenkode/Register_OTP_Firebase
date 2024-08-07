package com.example.companies.ui.chat;

import static com.google.firebase.firestore.Query.Direction.ASCENDING;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.model.FirmenModel;
import com.example.common.utils.FirestoreHelper;
import com.example.companies.SharedViewModel;
import com.example.companies.adapter.ChatRecyclerAdapter;
import com.example.companies.adapter.SystemMessageAdapter;
import com.example.companies.adapter.Tiket;
import com.example.companies.adapter.TiketAdapter;
import com.example.companies.databinding.FragmentChatBinding;
import com.example.companies.repository.ChatRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
* 1. При запуске происходит проверка, существует ли чат Строка 127
* 2. Далее в зависимости новый это чат или нет, создаётся комната 173
* 3.
* */
public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private Tiket tiket;
    private FirmenModel firmenModel;
    private ChatRepository chatRepository;
    private RecyclerView recyclerView;
    private RecyclerView chatRecycler;
    private ChatRecyclerAdapter adapter;
    private LatLng userLocation;
    private TiketAdapter tiketAdapter;
    private List<Tiket> tiketList;
    private EditText messageInput;
    private ImageButton sendButton,myBackButton;
    private boolean firstChat = true;
    private String chatroomId;
    private DatabaseReference messagesRef;
    private List<ChatMessageModel> chatMessages = new ArrayList<>();
    private static final String TAG = "ChatFragment";
    private SystemMessageAdapter systemMessageAdapter;
    private List<SystemChatMessageModel> systemMessages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"ResourceAsColor", "SuspiciousIndentation"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        // Инициализация SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        chatRepository = new ChatRepository(requireActivity(), new FirestoreHelper(), sharedViewModel);

        recyclerView = binding.recyclerChatTasks;
        chatRecycler = binding.chatRecyclerView;
        messageInput = binding.chatMessageInput;
        sendButton = binding.messageSendBtn;

        // Инициализация адаптера с пустым списком
        tiketAdapter = new TiketAdapter(new ArrayList<>(), this::onTiketClick);
        // Получаем chatId из аргументов или других источников

        return view;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //Пока не знаю нужно ли))))))
        sharedViewModel.getCompanyLocation().observe(getViewLifecycleOwner(),location ->{
            if (location != null) {
                userLocation = location;
            }
        });




        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(),tiket ->{
            if (tiket == null) {
                // Обработка случая, когда ticket равен null
                Log.e(TAG, "Ticket is null");
                return; // Остановка выполнения, если ticket равен null
            }

            // Проверка на наличие необходимых полей внутри объекта ticket
            if (tiket.getTicketId() == null) {
                Log.e(TAG, "Ticket ID is null");
                return;
            }
            sharedViewModel.clearPendingChatCreation();
            sharedViewModel.setChatFound(false);
            chatRepository.checkIfExisChat(tiket.getTicketId());
        } );




        sendButton.setOnClickListener(v -> {
            String messageContent = messageInput.getText().toString().trim();
            if(!messageContent.isEmpty()){
                if(firstChat){
                    chatRepository.sendFirstMessage(messageContent,false);
                    messageInput.setText("");
                }else{
                    chatRepository.sendMessageToExistingChat(sharedViewModel.getChatroomModel().getValue().getChatroomId(),messageContent);
                    messageInput.setText("");
                    hideKeyboard();
                }
                //chatRepository.sendMessage(messageContent);
                messageInput.setText("");
            }
        });
        sharedViewModel.getSystemChatMessages().observe(getViewLifecycleOwner(), message ->{
            setupRecyclerView();
            Log.e("ChatFragment", "message " + message.getButtons() + message.getMessage());
            systemMessages.add(message);
            systemMessageAdapter.notifyDataSetChanged();
        });

        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(), selectedTiket -> {
            tiketList = Collections.singletonList(selectedTiket);
            updateTiket(tiketList);
        });

        setupClickListeners(binding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tiketAdapter);

        //Проверка есть ли чат по данному тикету
        //Отправляем тикетИд и Ид компании
        sharedViewModel.getChatroomModel().observe(getViewLifecycleOwner(),chatroomModel -> {
            if(chatroomModel!= null){
                if(!chatroomModel.getChatroomId().toString().isEmpty()){
                    firstChat = false;
                }
                chatroomId = chatroomModel.getChatroomId().toString();
                setupChatRecyclerView();
                setupRealtimeDatabaseListeners();
            }

        });

    }
    private void onTiketClick(Tiket tiket) {
    }

    private void updateTiket(List<Tiket> tiketList){
        TiketAdapter tiketAdapter = new TiketAdapter(tiketList, new TiketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tiket tiket) {
            }
        });
        recyclerView.setAdapter(tiketAdapter);
    }
    private void setupRealtimeDatabaseListeners() {

        messagesRef = FirebaseDatabase.getInstance().getReference("chats").child(chatroomId).child("messages");
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ChatMessageModel newMessage = dataSnapshot.getValue(ChatMessageModel.class);
                chatMessages.add(newMessage);
                adapter.notifyItemInserted(chatMessages.size() - 1);
                chatRecycler.scrollToPosition(chatMessages.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Обработка изменений сообщений
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Обработка удаления сообщений
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Обработка перемещения сообщений
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок
            }
        });
    }

    private void setupRecyclerView() {
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        systemMessageAdapter = new SystemMessageAdapter(getContext(),sharedViewModel,systemMessages);

        chatRecycler.setAdapter(systemMessageAdapter);
    }

    private void setupChatRecyclerView() {
        chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<ChatMessageModel> options = getOptions();
        adapter = new ChatRecyclerAdapter(options, getContext());

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        chatRecycler.setLayoutManager(manager);
        chatRecycler.setAdapter(adapter);
        // Запускаем прослушивание изменений в данных
        adapter.startListening();
    }
    private FirestoreRecyclerOptions<ChatMessageModel> getOptions() {
        if (chatroomId == null) {
            throw new IllegalStateException("Chat ID must be set before calling getOptions()");
        }
        Query query = FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chatroomId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        return new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class)
                .build();

    }
    private void getChatsFromTicket() {
        // Реализация метода здесь
    }


    private void openChat(String chatId) {
        // Реализация метода здесь
    }

    private void createChat() {
        // Реализация метода здесь
    }

    private void setupClickListeners(FragmentChatBinding binding){
        binding.myBackButtonChat.setOnClickListener(v -> {
            if(getActivity() != null){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Очистка данных о чате в sharedViewModel
        sharedViewModel.clearChatData();
        sharedViewModel.destroySelectedTicket();
        // Дополнительные очистки, если необходимо
    }
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

}


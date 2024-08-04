package com.example.companies.ui.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.companies.adapter.Tiket;
import com.example.companies.adapter.TiketAdapter;
import com.example.companies.databinding.FragmentChatBinding;
import com.example.companies.repository.ChatRepository;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private NavController navController;
    private SharedViewModel sharedViewModel;
    private Tiket tiket;
    private FirmenModel firmenModel;
    private ChatRepository chatRepository;
    private RecyclerView recyclerView;
    private RecyclerView chatRecycler;
    private LatLng userLocation;
    private TiketAdapter tiketAdapter;
    private List<Tiket> tiketList;

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
        chatRepository = new ChatRepository(new FirestoreHelper(), sharedViewModel);

        recyclerView = binding.recyclerChatTasks;
        chatRecycler = binding.chatRecyclerView;

        // Инициализация адаптера с пустым списком
        tiketAdapter = new TiketAdapter(new ArrayList<>(), null, this::onTiketClick);



        sharedViewModel.getCompanyLocation().observe(getViewLifecycleOwner(),location ->{
            if (location != null) {
                userLocation = location;
            }
        });


        return view;

    }

    private void onTiketClick(Tiket tiket) {
    }
    private void updateTiket(List<Tiket> tiketList){
            TiketAdapter tiketAdapter = new TiketAdapter(tiketList, userLocation, new TiketAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Tiket tiket) {
                }
            });
        recyclerView.setAdapter(tiketAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(), selectedTiket -> {

            tiketList = Collections.singletonList(selectedTiket);
            updateTiket(tiketList);
        });

        setupClickListeners(binding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tiketAdapter);

        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(),tiket ->{

            chatRepository.updateChatsForTicket(tiket.getTicketId());
        } );


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

}


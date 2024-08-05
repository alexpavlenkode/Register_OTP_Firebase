package com.example.companies.ui.nachrichten;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.utils.FirestoreHelper;
import com.example.companies.SharedViewModel;
import com.example.companies.adapter.NachrichtenAdapter;
import com.example.companies.adapter.Tiket;
import com.example.companies.adapter.User;
import com.example.companies.databinding.FragmentNachrichtenBinding;
import com.example.companies.repository.NachrichtenRepository;
import com.example.companies.repository.TaskRepository;
import com.example.companies.ui.chat.NachrichtenModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

import java.util.ArrayList;
import java.util.List;

public class NachrichtenFragment extends Fragment {
    private SharedViewModel viewModel;
    private RecyclerView nachrichtenRecyclerView;
    private List<User> userList;
    private FragmentNachrichtenBinding binding;
    private SharedViewModel sharedViewModel;
    private TaskRepository taskRepository;
    private NavController navController;
    private NachrichtenRepository nachrichtenRepository;
    private static final String TAG = "NachrichtenFragment";
    private List allRooms;
    private NachrichtenAdapter nachrichtenAdapter;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NachrichtenViewModel dashboardViewModel =
                new ViewModelProvider(this).get(NachrichtenViewModel.class);

        binding = FragmentNachrichtenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        nachrichtenRepository = new NachrichtenRepository(new FirestoreHelper(),sharedViewModel);
        taskRepository = new TaskRepository(getActivity());

        //// Настройка RecyclerView
        nachrichtenRecyclerView = binding.recyclerViewTasks;

        nachrichtenAdapter = new NachrichtenAdapter(allRooms, new NachrichtenAdapter.OnNachrichtClickListener() {
            @Override
            public void onItemClick(NachrichtenModel nachrichtenModel) {
                // Передача выбранного тикета в SharedViewModel
                // Предполагаем, что в nachrichtenModel содержится информация о ticketId
                String ticketId = nachrichtenModel.getTicketId();
                // Передача реализации интерфейса обратного вызова
                taskRepository.getTasksByTicketId(ticketId, new TaskRepository.OnTiketsFetchedListener() {
                    @Override
                    public void onTasksFetched(Tiket tiket) {
                        // Установите выбранный тикет в SharedViewModel
                        sharedViewModel.selectTiket(tiket);
                        Log.e(TAG, "Ticket " + tiket);
                        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(), existTiket -> {
                        NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                        navController.navigate(com.example.companies.R.id.chatRoomFragment);
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


            }
        });
        List<NachrichtenModel> allRooms = new ArrayList<>();

        sharedViewModel.getUserProfile().observe(getViewLifecycleOwner(),firmenProfill ->{

            nachrichtenRepository.getAllChats(firmenProfill.getUserId());
        });
        sharedViewModel.getNachrichtenModels().observe(getViewLifecycleOwner(),nachrichten ->{
            if (nachrichten != null) {
                allRooms.clear();
                allRooms.addAll(nachrichten);  // Заполняем allRooms новыми данными

                if (nachrichtenAdapter != null) {
                    updateRoomAdapter(allRooms);
                } else {
                    Log.e(TAG, "nachrichtenAdapter is Null");
                }
            }

        });
        FlexboxLayout chipGroupWichtigkeitNachrichten = binding.chipGroupWichtigkeitNachrichten;
        FlexboxLayout selectedWichtigkeitNachrichten = binding.selectedChipGroupWichtigkeitNachrichten;
        
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_all), ContextCompat.getColor(getContext(), com.example.common.R.color.white), com.example.common.R.color.messages_all);
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_inprogress),ContextCompat.getColor(getContext(), com.example.common.R.color.black), com.example.common.R.color.messages_inprogress);
        addChipToGroup(chipGroupWichtigkeitNachrichten,getString(com.example.common.R.string.messages_completed),ContextCompat.getColor(getContext(), com.example.common.R.color.black), com.example.common.R.color.messages_completed);



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        nachrichtenRecyclerView.setAdapter(nachrichtenAdapter);
        nachrichtenRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nachrichtenRecyclerView.setAdapter(nachrichtenAdapter);
    }


    private void updateRoomAdapter(List<NachrichtenModel> nachrichtenModel){

        NachrichtenAdapter nachrichtenAdapter = new NachrichtenAdapter(nachrichtenModel, new NachrichtenAdapter.OnNachrichtClickListener() {
            @Override
            public void onItemClick(NachrichtenModel nachrichtenModel) {
                // Передача выбранного тикета в SharedViewModel
                // Предполагаем, что в nachrichtenModel содержится информация о ticketId
                String ticketId = nachrichtenModel.getTicketId();
                // Передача реализации интерфейса обратного вызова
                taskRepository.getTasksByTicketId(ticketId, new TaskRepository.OnTiketsFetchedListener() {
                    @Override
                    public void onTasksFetched(Tiket tiket) {
                        // Установите выбранный тикет в SharedViewModel
                        sharedViewModel.selectTiket(tiket);
                        Log.e(TAG, "Ticket " + tiket);
                        sharedViewModel.getSelectedTiket().observe(getViewLifecycleOwner(), existTiket -> {
                            NavController navController = Navigation.findNavController(getActivity(), com.example.companies.R.id.nav_host_fragment_activity_user);
                            navController.navigate(com.example.companies.R.id.chatRoomFragment);
                        });
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


            }
        });
        nachrichtenRecyclerView.setAdapter(nachrichtenAdapter);
    }

    private void addChipToGroup(FlexboxLayout chipGroup, String text, int textColor,int iconColor) {
        Chip chip = new Chip(getActivity());
        chip.setText(text);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setChipIconVisible(true);  // Ensure the icon is visible
        chip.setTextColor(textColor);
        chip.setChipBackgroundColorResource(iconColor);

        ChipDrawable chipDrawable = (ChipDrawable) chip.getChipDrawable();
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadius(40f); // Устанавливаем радиус закругления в пикселях
            chipDrawable.setChipStrokeColorResource(android.R.color.transparent); // Убираем обводку

        }
        chip.setChipDrawable(chipDrawable);

        chipGroup.addView(chip);
        // Создаем параметры LayoutParams и задаем отступы
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) getResources().getDimension(com.example.common.R.dimen.chip_intent_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        chip.setLayoutParams(layoutParams);
    }

    public void onStart(){
        super.onStart();
        sharedViewModel.destroySelectedTicket();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
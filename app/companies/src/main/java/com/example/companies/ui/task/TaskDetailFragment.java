package com.example.companies.ui.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.companies.databinding.FragmentTaskDetailBinding;

public class TaskDetailFragment extends Fragment {
    private FragmentTaskDetailBinding binding;
    private TaskDetailViewModel viewModel;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setupClickListeners(binding);

        return view;
    }
    private void setupClickListeners(FragmentTaskDetailBinding binding) {
        binding.myBackButtonTaskDetail.setOnClickListener(v -> {
            // Закрыть текущий фрагмент
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Дополнительный обработчик для TextView
        binding.backTextTaskDetail.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

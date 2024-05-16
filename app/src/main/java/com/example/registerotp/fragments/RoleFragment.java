package com.example.registerotp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentRoleBinding;


public class RoleFragment extends Fragment {
    private NavController navController;
    private FragmentRoleBinding b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentRoleBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        b.cardCustomer.setOnClickListener(v -> {
            navController.navigate(R.id.action_roleFragment_to_loginCustomerFragment);
        });

        b.cardExecutor.setOnClickListener(v -> {
            navController.navigate(R.id.action_roleFragment_to_loginExecutorFragment);
        });
    }
}
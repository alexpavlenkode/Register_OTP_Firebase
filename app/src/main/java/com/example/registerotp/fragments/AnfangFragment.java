package com.example.registerotp.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentAnfangBinding;
import com.example.registerotp.model.KundenModell;
import com.hbb20.CountryCodePicker;

import java.util.Locale;


public class AnfangFragment extends Fragment {
    private FragmentAnfangBinding binding;
    private NavController navController;
    private KundenModell kundenModell;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentAnfangBinding.inflate(inflater, container, false);
        //возвращает корневое представление этого макета, чтобы система Android могла отобразить его

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        //устанавливает слушатель нажатий
        binding.einlogenBtn.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            navController.navigate(R.id.id_action_to_einlogen);

        });
        binding.linkToRegistrActivity.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            navController.navigate(R.id.id_action_to_regestrierung);

        });


    }

    //Methode zur Animation der Änderung des unteren Paddings


}

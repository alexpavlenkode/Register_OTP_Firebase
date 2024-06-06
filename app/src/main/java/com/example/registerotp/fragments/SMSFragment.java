package com.example.registerotp.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.databinding.FragmentEnterSmsBinding;
import com.example.registerotp.databinding.FragmentLoginBinding;

public class SMSFragment extends Fragment {
    private FragmentEnterSmsBinding binding;
    private NavController navController;

    //Этот метод вызывается системой Android, когда фрагмент должен создать свой пользовательский интерфейс
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Раздуваем макет
        binding = FragmentEnterSmsBinding.inflate(inflater, container, false);

        EditText editText1 = binding.editText1;
        editText1.requestFocus();
        WindowInsetsControllerCompat insetsController = ViewCompat.getWindowInsetsController(editText1);
        if (insetsController != null) {
            insetsController.show(WindowInsetsCompat.Type.ime());
        }
        //возвращает корневое представление этого макета, чтобы система Android могла отобразить его
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        // Добавляем слушатели текста для каждого поля
        binding.editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText2.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Для последнего EditText
        binding.editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Ничего не делаем, так как это последнее поле
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


}

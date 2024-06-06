package com.example.registerotp.fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentLoginBinding;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

public class LoginFragment extends Fragment {

    private NavController navController;
    private FragmentLoginBinding binding;
    private NestedScrollView nestedScrollView;
    private LoginFragment loginFragment;
    private CountryCodePicker ccp;
    private EditText editTextCarrierNumber;


    //Этот метод вызывается системой Android, когда фрагмент должен создать свой пользовательский интерфейс
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Раздуваем макет
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        //возвращает корневое представление этого макета, чтобы система Android могла отобразить его


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


        //устанавливает слушатель нажатий
        binding.smsActivationBtn.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            if(isValid()){
                navController.navigate(R.id.id_action_to_smsActivationFragment);
            }
        });

        loginFragment = this;
        Locale primaryLocale = loginFragment.getResources().getConfiguration().getLocales().get(0);
        String country = primaryLocale.getCountry();
        System.out.println("Country " + country);

        ccp = (CountryCodePicker) binding.ccp;
        editTextCarrierNumber = (EditText) binding.inputPhoneNumber;
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        // Устанавливаем слушатель изменения WindowInsets для вашего представления
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            // Получаем доступ к NestedScrollView из привязанного представления
            nestedScrollView = binding.nestedScrollView;

            // Определяем, видима ли в данный момент клавиатура
            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            // Получаем высоту клавиатуры
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

            // Изменяем внутренние отступы NestedScrollView в зависимости от видимости клавиатуры
            animateNestedScrollViewPaddingChange(nestedScrollView, imeVisible ? imeHeight : 0);

            // Возвращаем insets, чтобы они могли быть потреблены другими представлениями
            return insets;
        });

    }
    //Methode zur Animation der Änderung des unteren Paddings
    private void animateNestedScrollViewPaddingChange(NestedScrollView nestedScrollView, int padding) {

        // Animiert die Änderung des Paddings
        ValueAnimator animator = ValueAnimator.ofInt(nestedScrollView.getPaddingBottom(), padding);
        //Setzt die Animationsdauer auf 100 Millisekunden.
        animator.setDuration(100);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();
                //Ändert das Padding während der Animation.
                nestedScrollView.setPadding(nestedScrollView.getPaddingLeft(), nestedScrollView.getPaddingTop(), nestedScrollView.getPaddingRight(), currentValue);
            }
        });
        animator.start();
    }

    private boolean isValid(){
        if(binding.txtUsername.getEditText().getText().toString().equals("")|| binding.txtUsername.getEditText().getText().toString().length() < 4){

            binding.txtUsername.setError(getString(R.string.invalid_name));
            return false;
        }
        return true;
    }


}

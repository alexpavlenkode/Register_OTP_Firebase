package com.example.registerotp.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentStartRegistrationBinding;
import com.example.registerotp.utils.AndroidUtil;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegFragment extends Fragment {
    private NavController navController;
    private NestedScrollView nestedScrollView;
    private FragmentStartRegistrationBinding binding;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private boolean isPasswordVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentStartRegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        //Устанавливаем выбор роли
        MaterialRadioButton privatperson = binding.privatperson;
        MaterialRadioButton firma = binding.firma;
        privatperson.setChecked(true);
        firma.setChecked(false);
        passwordTextInputLayout = binding.passwort;
        passwordEditText = binding.eingabePasswort;

        //Скрываем пароль свёздами, показываем при нажатии
        passwordTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectionStart = passwordEditText.getSelectionStart();
                int selectionEnd = passwordEditText.getSelectionEnd();

                if (isPasswordVisible) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isPasswordVisible = false;
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isPasswordVisible = true;

                    // Hide password again after a short delay
                    new Handler().postDelayed(() -> {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        isPasswordVisible = false;
                        passwordEditText.setSelection(selectionStart, selectionEnd);
                    }, 1000); // 1 second delay
                }

                // Move the cursor to the end of the input
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        //Слушатели для выбора роли
        privatperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firma.setChecked(false); // Снимаем выделение с другого RadioButton
                privatperson.setChecked(true); // Устанавливаем выделение для текущего RadioButton
                binding.firmenName.setVisibility(View.GONE);
                binding.loginName.setVisibility(View.VISIBLE);
                binding.regestrierenBtn.setText(getResources().getString(R.string.reg_button_text));
            }
        });

        firma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privatperson.setChecked(false); // Снимаем выделение с другого RadioButton
                firma.setChecked(true); // Устанавливаем выделение для текущего RadioButton
                binding.firmenName.setVisibility(View.VISIBLE);
                binding.loginName.setVisibility(View.GONE);
                binding.regestrierenBtn.setText(getResources().getString(R.string.next_button_text));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) ->{
            nestedScrollView = binding.nestedStartRegistration;

            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

            animatedNestedScrollViewPaddingChange(nestedScrollView, imeVisible ? imeHeight : 0);
            return insets;

        });

    }

    private void animatedNestedScrollViewPaddingChange(NestedScrollView nestedScrollView, int padding){
        ValueAnimator animator = ValueAnimator.ofInt(nestedScrollView.getPaddingBottom(), padding);
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
}



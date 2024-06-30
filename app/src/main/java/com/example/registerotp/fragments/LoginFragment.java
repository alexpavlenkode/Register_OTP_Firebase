package com.example.registerotp.fragments;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.registerotp.databinding.FragmentLoginBinding;
import com.example.registerotp.model.KundenModell;
import com.example.registerotp.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

public class LoginFragment extends Fragment {

    private NavController navController;
    private FragmentLoginBinding binding;
    private NestedScrollView nestedScrollView;
    private LoginFragment loginFragment;
    private EditText editTextCarrierNumber;

    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private boolean isPasswordVisible = false;
    private FirebaseAuth mAuth;



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
        mAuth = FirebaseAuth.getInstance();

        passwordTextInputLayout = binding.passwort;
        passwordEditText = binding.eingabePasswort;
        //Instanz der KundenModell-Klasse
        //kundenModell = new KundenModell();

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

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) ->{
            nestedScrollView = binding.nestedScrollViewGetSMS;

            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

            if (imeVisible) {
                //binding.btnAddImage.setVisibility(View.GONE);
                animateNestedScrollViewPaddingChange(nestedScrollView, imeHeight);
            } else {
                //binding.btnAddImage.setVisibility(View.VISIBLE);
                animateNestedScrollViewPaddingChange(nestedScrollView, 0);
            }
            return insets;

        });

        //устанавливает слушатель нажатий
        binding.einlogenBtn.setOnClickListener(clickedView  -> {
            if(isValid()){
                String mail = binding.eMail.getEditText().getText().toString();
                String password = binding.passwort.getEditText().getText().toString();
                mAuth.signInWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    navController.navigate(R.id.id_action_to_home_page_login_sucefull);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    ///Обработать ошибку!!!!
                                }
                            }
                        });
                    }

        });
        binding.linkToRegistrActivity.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            navController.navigate(R.id.id_action_to_regestrierung);

        });

        // Устанавливаем слушатель изменения WindowInsets для вашего представления
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            // Получаем доступ к NestedScrollView из привязанного представления
            nestedScrollView = binding.nestedScrollViewGetSMS;

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
        String mail = binding.eingabeMail.getText().toString();
        String password = binding.eingabePasswort.getText().toString();

        if (!isValidEmail(mail)) {
            binding.eMail.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_email_name));
            return false;
            }
        if (password.isEmpty()) {
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_passwort));
            return false;
        }
        return true;
    }
    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email != null && email.matches(emailPattern);
    }
}

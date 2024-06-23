package com.example.registerotp.fragments;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.registerotp.model.FirmenModel;
import com.example.registerotp.model.KundenModell;
import com.example.registerotp.utils.AndroidUtil;
import com.example.registerotp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegFragment extends Fragment {
    private NavController navController;
    private NestedScrollView nestedScrollView;
    private FragmentStartRegistrationBinding binding;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private boolean isPasswordVisible = false;
    private ActivityResultLauncher<Intent> bildAuswachLauncher;
    private Uri bildUrl;
    private KundenModell kundenModell;
    private FirmenModel firmenmodell;
    private FirebaseAuth mAuth;

    //Этот метод вызывается системой Android, когда фрагмент должен создать свой пользовательский интерфейс
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        kundenModell = bundle.getParcelable("kundenModell");
        bildAuswachLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                if(data != null && data.getData() != null){
                    bildUrl = data.getData();
                    AndroidUtil.setProfilePic(getContext(), bildUrl, binding.btnAddImage);
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Раздуваем макет
        binding = FragmentStartRegistrationBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);


        navController = Navigation.findNavController(view);
        passwordTextInputLayout = binding.passwort;
        passwordEditText = binding.eingabePasswort;
        //Устанавливаем выбор роли
        MaterialRadioButton privatperson = binding.privatperson;
        MaterialRadioButton firma = binding.firma;

        privatperson.setChecked(true);
        firma.setChecked(false);


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
        //Слушатели для выбора роли
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
        //Слушатель для клавиатуры
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) ->{
            nestedScrollView = binding.nestedStartRegistration;

            boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
            int imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

            if (imeVisible) {
                //binding.btnAddImage.setVisibility(View.GONE);
                animatedNestedScrollViewPaddingChange(nestedScrollView, imeHeight);
            } else {
                //binding.btnAddImage.setVisibility(View.VISIBLE);
                animatedNestedScrollViewPaddingChange(nestedScrollView, 0);
            }
            return insets;

        });

        //Слушатель выбора фото
        binding.btnAddImage.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512).createIntent(intent -> {
                bildAuswachLauncher.launch(intent);
                return null;
            });
        });

        binding.regestrierenBtn.setOnClickListener(clickedView  -> {
            //После нажатия отправляем по destination из Navigation
            kundenModell.setEmail(binding.eMail.getEditText().getText().toString());
            if(isValid()){
                if(binding.privatperson.isChecked()){
                    kundenModell.setLoginName(binding.loginName.getEditText().getText().toString());
                    kundenModell.setRegComplet(true);

                    FirebaseUtil.currentUserDetails().set(kundenModell).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String mail = binding.eMail.getEditText().getText().toString();
                                String password = binding.passwort.getEditText().getText().toString();

                                mAuth.createUserWithEmailAndPassword(mail, password)
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Bundle args = new Bundle();
                                                    args.putParcelable("kundenModell", kundenModell);
                                                    navController.navigate(R.id.id_action_to_mein_menu, args);
                                                } else {
                                                    try {
                                                        throw task.getException();
                                                    } catch (FirebaseAuthUserCollisionException e) {
                                                        // Этот email уже используется
                                                        AndroidUtil.showToast(getActivity(), "Пользователь с таким email уже существует.");
                                                    } catch (Exception e) {
                                                        // Общая ошибка
                                                        AndroidUtil.showToast(getActivity(), "Ошибка регистрации: " + e.getMessage());
                                                    }
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
                else if (binding.firma.isChecked()){
                    firmenmodell.setCompanyName(binding.firmenName.getEditText().toString());
                    firmenmodell.seteMail(binding.eMail.getEditText().toString());
                    String selectedImgUrl = bildUrl.toString();
                    firmenmodell.setImageUrl(selectedImgUrl);
                    if(isValid()){
                        Bundle args = new Bundle();
                        args.putParcelable("firmenModell",firmenmodell);
                        navController.navigate(R.id.id_action_to_regestrierung_firma, args);
                    }
                }

            }
        });

    }
    //Появление клавиатуры
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

    private boolean isValid(){

        String password = binding.eingabePasswort.getText().toString();
        String confirmPassword = binding.eingabePasswortSecond.getText().toString();

        //Фирама или персона
        if (binding.privatperson.isChecked()){
            //Если поле Персона пустое
            if(binding.eingabeloginName.getText().toString().equals("") || binding.eingabeloginName.getText().toString().length() < 4 ){
                AndroidUtil.showToast(getActivity(), getString(R.string.invalid_login_name));
                return false;
            }
        } else if (binding.firma.isChecked()) {
            if(binding.eingabefirmenName.getText().toString().equals("") || binding.eingabefirmenName.getText().toString().length() < 4 ){
                AndroidUtil.showToast(getActivity(), getString(R.string.invalid_firmen_name));
                return false;
            }
        }
        //Проверяем емайл
        if (!isValidEmail(binding.eingabeMail.getText().toString())) {
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_email_name));
            return false;
        }

        //Проверяем пароль
        //1. Не должно быть пустым
        //2. Не должен быть лёгким
        //3. Пароли должны совпадать
        // Пароль пустой


        if (password.isEmpty()) {
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_passwort));
            return false;
        }
        if (!isPasswordStrong(password)) {
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_passwort_strong));
            return false;
        }
        if (confirmPassword.isEmpty()) {
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_passwort_second));
            return false;
        }
        if (!password.equals(confirmPassword)) {
            binding.passwort.setError("");
            binding.passwortSecond.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_passwort_ident_check));
            // Пароли не совпадают
            return false;
        }
        return true;
    }

    public boolean isPasswordStrong(String password) {
        if (password == null) {
            return false;
        }
        // Минимальная длина пароля
        int minLength = 8;
        // Регулярное выражение для проверки наличия цифры
        String digitPattern = ".*\\d.*";
        // Регулярное выражение для проверки наличия заглавной буквы
        String upperCasePattern = ".*[A-Z].*";
        // Регулярное выражение для проверки наличия строчной буквы
        String lowerCasePattern = ".*[a-z].*";

        return password.length() >= minLength &&
                password.matches(digitPattern) &&
                password.matches(upperCasePattern) &&
                password.matches(lowerCasePattern);
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email != null && email.matches(emailPattern);
    }
}



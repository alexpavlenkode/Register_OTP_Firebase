package com.example.registerotp.fragments;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.InputType;
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
import com.example.registerotp.databinding.FragmentSetKeywordsBinding;
import com.example.registerotp.databinding.FragmentSetMailPasswortBinding;
import com.example.registerotp.databinding.FragmentStartRegistrationFirmaBinding;
import com.example.registerotp.model.FirmenModel;
import com.example.registerotp.model.KundenModell;
import com.example.registerotp.model.LoginModell;
import com.example.registerotp.utils.AndroidUtil;
import com.example.registerotp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class fragment_set_mail_passwort extends Fragment {

    private NavController navController;
    private FirmenModel firmenmodell;
    private LoginModell loginModell;
    private FragmentSetMailPasswortBinding binding;
    private boolean isPasswordVisible = false;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;
    private NestedScrollView nestedScrollView;
    private ActivityResultLauncher<Intent> bildAuswachLauncher;
    private Uri bildUrl;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        firmenmodell = bundle.getParcelable("firmenModell");

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
        //Вставляем полученные данные
        binding = FragmentSetMailPasswortBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding.eingabeMail.setText(firmenmodell.geteMail());
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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

        binding.regestrierenBtnFirma.setOnClickListener(v -> {
            if(isValid()){


                String mail = binding.eMail.getEditText().getText().toString();
                String password = binding.passwort.getEditText().getText().toString();
                loginModell = new LoginModell();

                firmenmodell.seteMail(mail.toString());



                mAuth.createUserWithEmailAndPassword(mail, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Timestamp currentTimestamp = Timestamp.now();
                                    loginModell.setCreatedTimestamp(currentTimestamp);
                                    loginModell.setRegComplet(true);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getUid();
                                    loginModell.setUserId(userId.toString());
                                    loginModell.setPrivatPerson(false);

                                    if (user != null) {

                                        // Создание документа пользователя в Firestore
                                        createUserDocument(userId,"companies", firmenmodell).addOnCompleteListener(task1 -> {
                                            if (task.isSuccessful()) {
                                                loginModell.setRegComplet(true);
                                                createUserDocument(userId,"id", loginModell).addOnCompleteListener(task2 -> {
                                                    if (task2.isSuccessful()) {
                                                        Bundle args = new Bundle();
                                                        args.putParcelable("companies", firmenmodell);
                                                        args.putParcelable("id", loginModell);
                                                        navController.navigate(R.id.id_regestriert_privat_kunde, args);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }else {
                                    // Обработка ошибок
                                    System.err.println("Error registering user: " + task.getException());
                                }
                            }
                        });



            }

        });


    }

    private <T extends Parcelable> Task<Void> createUserDocument(String userId, String collection, T model){
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        db.collection(collection).document(userId)
                .set(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            taskCompletionSource.setResult(null);

                        } else {
                            taskCompletionSource.setException(task.getException());
                            System.err.println("Error creating user document: " + task.getException());
                        }
                    }

                });
        return taskCompletionSource.getTask();
    }

    private boolean isValid(){
        String mail = binding.eingabeMail.getText().toString();
        String password = binding.eingabePasswort.getText().toString();
        String confirmPassword = binding.eingabePasswortSecond.getText().toString();

        if (!isValidEmail(mail)) {
            binding.eMail.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_email_name));
            return false;
        }
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

}

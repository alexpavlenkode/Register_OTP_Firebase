package com.example.registerotp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentEnterSmsBinding;
import com.example.registerotp.model.KundenModell;
import com.example.registerotp.utils.AndroidUtil;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SMSFragment extends Fragment {
    private FragmentEnterSmsBinding binding;
    private NavController navController;
    private KundenModell kundenModell;
    private FirebaseAuth mAuth;
    private Timer teimer;
    private Long timeOutSeconds = 60L;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    //Этот метод вызывается системой Android, когда фрагмент должен создать свой пользовательский интерфейс
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        kundenModell = bundle.getParcelable("kundenModell");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Раздуваем макет
        binding = FragmentEnterSmsBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        EditText editText1 = binding.editText1;
        editText1.setText("");
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

        //SMS versenden
        smsVersenden(kundenModell.getPhone(), false);
        // Добавляем слушатели текста для каждого поля
        binding.editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText2.setText("");
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
                    binding.editText3.setText("");
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
                    binding.editText4.setText("");
                    binding.editText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText5.setText("");
                    binding.editText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    binding.editText6.setText("");
                    binding.editText6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Для последнего EditText
        binding.editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String enteredSMS = eingegebeneSMS();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredSMS);
                dasEinlogen(credential);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private String eingegebeneSMS() {
        return binding.editText1.getText().toString() +
                binding.editText2.getText().toString() +
                binding.editText3.getText().toString() +
                binding.editText4.getText().toString() +
                binding.editText5.getText().toString() +
                binding.editText6.getText().toString();
    }
    /**
     * Sendet eine OTP (One Time Password) an die angegebene Telefonnummer.
     * Startet den Resend-Timer und setzt den Status auf "in Progress".
     *
     * @param telefonNummer Die Telefonnummer, an die das OTP gesendet wird.
     * @param istResendet    Gibt an, ob es sich um einen erneuten Versand handelt.
     */
    void smsVersenden(String telefonNummer, boolean istResendet) {
        //Resend-Timer starten
        starteErneutTimer();
        //Setzt den Status auf "in Progress", um anzuzeigen, dass die Anfrage bearbeitet wird
        setzeInBearbeitung(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(telefonNummer)
                        .setTimeout(timeOutSeconds, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                AndroidUtil.showToast(getActivity(), "onVerificationCompleted");
                                //dasEinlogen(phoneAuthCredential);
                                setzeInBearbeitung(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getActivity(), "OTP verification failed");
                                setzeInBearbeitung(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationsSMS, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationsSMS, forceResendingToken);
                                //Сохранение кода верификации, который был отправлен
                                verificationCode = verificationsSMS;
                                // Сохранение токена для повторной отправки в случае необходимости
                                resendingToken = forceResendingToken;
                                // Попытка показать тост-сообщение пользователю о том, что OTP был успешно отправлен
                                try {
                                    AndroidUtil.showToast(requireActivity(), "OTP sent successfully");// todo this is exception
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                setzeInBearbeitung(false);
                            }
                        });
        if (istResendet) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }
    void dasEinlogen(PhoneAuthCredential phoneAuthCredential) {
        //login and go to next activity
        setzeInBearbeitung(true);

        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setzeInBearbeitung(false);
                if (task.isSuccessful()) {
                    //setzeLoginname();
                    Bundle args = new Bundle();
                    args.putParcelable("kundenModell", kundenModell);
                    navController.navigate(R.id.id_action_start_reg_after_sms,args);
                } else {
                    AndroidUtil.showToast(getActivity(), "OTP verification failed");
                }
            }
        });
    }
    private void starteErneutTimer() {
        //Deaktiviert den Button zum erneuten Senden der SMS
        binding.ntfNoSms.setEnabled(false);
        //Instanz eines Timers
        teimer = new Timer();
        teimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeOutSeconds--;
                //Aktualisiert den Text der TextView, um die verbleibende Zeit anzuzeigen
                binding.resendSmsTextview.setText("Du kannst die SMS in " + timeOutSeconds + " Sekunden erneut senden");
                if (timeOutSeconds <= 0) {
                    timeOutSeconds = 60L;
                    teimer.cancel();

                    //Führt eine Aktion im UI-Thread aus, um den Button wieder zu aktivieren
                    requireActivity().runOnUiThread(() -> {
                        binding.ntfNoSms.setEnabled(true);
                    });
                }
            }
        }, 0, 1000);
    }

    void setzeInBearbeitung(boolean inProgress) {
        if (inProgress) {
            //ProgressBar nicht benötigt
            //binding.loginProgressBar.setVisibility(View.VISIBLE);
        } else {
            //ProgressBar nicht benötigt
            //binding.loginProgressBar.setVisibility(View.GONE);
            //hier wird geprüfft ob die SMS korrekt ist!
        }
    }

    private void setzeLoginname() {
        // Устанавливает состояние выполнения в true, чтобы показать, что процесс начался
        setzeInBearbeitung(true);

        //1. Проверяем есть ли пользователь с таким номеро

        /*// Устанавливает уникальный идентификатор пользователя и время создания для модели клиента
        kundenModell.setUserId(FirebaseUtil.currentUserId());
        kundenModell.setCreatedTimestamp(Timestamp.now());

        // Сохраняет текущие детали пользователя в Firebase
        FirebaseUtil.currentUserDetails().set(kundenModell).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Устанавливает состояние выполнения в false, чтобы показать, что процесс завершился
                setzeInBearbeitung(false);
                // Проверяет, успешно ли завершена задача
                if (task.isSuccessful()) {
                    Bundle args = new Bundle();
                    args.putParcelable("kundenModell", kundenModell);
                    navController.navigate(R.id.id_action_start_reg_after_sms,args);
                }else {
                    // После нужно доделать обработку!!!!----------------------------------------------------- Уход из прилоения, отправка о ошибке
                    Log.e("FirebaseError", "Ошибка при сохранении данных", task.getException());
                    AndroidUtil.showToast(getActivity(), "Ошибка при сохранении данных, повторная попытка...");
                    new Handler().postDelayed(() -> setzeLoginname(), 5000);
                }
            }
        });*/
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        teimer.cancel();
    }


}

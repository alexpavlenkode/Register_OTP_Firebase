package com.example.registerotp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.registerotp.LoginActivity;
import com.example.registerotp.MainActivity;
import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentLoginCustomerBinding;
import com.example.registerotp.databinding.FragmentOTPBinding;
import com.example.registerotp.model.CustomerModel;
import com.example.registerotp.model.ExecutorModel;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OTPFragment extends Fragment {
    private FragmentOTPBinding b;
    private NavController navController;
    private FirebaseAuth mAuth;
    private String phoneNumber;
    private Long timeoutSeconds = 60L;
    private String verificationCode;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private CustomerModel customerModel;
    private ExecutorModel executorModel;
    private boolean isCustomer = false;

    private Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle.containsKey("customerModel")) {
            customerModel = bundle.getParcelable("customerModel");
            isCustomer = true;
        } else if (bundle.containsKey("executorModel")) {
            executorModel = bundle.getParcelable("executorModel");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentOTPBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        navController = Navigation.findNavController(view);

        phoneNumber = isCustomer ? customerModel.getPhone() : executorModel.getPhone();

        sendOtp(phoneNumber, false);


        b.btnContinue.setOnClickListener(v -> {
            if (b.etOtp.getText().toString().isEmpty()){
                AndroidUtil.showToast(requireActivity(), getString(R.string.field_must_be_not_empty));
            }else{
                String enteredOtp = b.etOtp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                signIn(credential);
            }
        });

        b.resendOtpTextview.setOnClickListener((v) -> {
            sendOtp(phoneNumber, true);
        });
    }


    void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(requireActivity())
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getActivity(), "OTP verification failed");
                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                try {
                                    AndroidUtil.showToast(requireActivity(), "OTP sent successfully");// todo this is exception
                                } catch (IllegalStateException e) {
                                    e.printStackTrace();
                                }
                                setInProgress(false);
                            }
                        });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            b.loginProgressBar.setVisibility(View.VISIBLE);
            b.btnContinue.setVisibility(View.GONE);
        } else {
            b.loginProgressBar.setVisibility(View.GONE);
            b.btnContinue.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential) {
        //login and go to next activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    setUsername();
                } else {
                    AndroidUtil.showToast(getActivity(), "OTP verification failed");
                }
            }
        });
    }

    private void startResendTimer() {
        b.resendOtpTextview.setEnabled(false);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                b.resendOtpTextview.setText("Повторная отправка смс через " + timeoutSeconds + " сек");
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L;
                    timer.cancel();
                    requireActivity().runOnUiThread(() -> {
                        b.resendOtpTextview.setEnabled(true);
                    });
                }
            }
        }, 0, 1000);
    }

    private void setUsername() {
        setInProgress(true);

        if (isCustomer) {
            customerModel.setUserId(FirebaseUtil.currentUserId());
            customerModel.setCreatedTimestamp(Timestamp.now());

            FirebaseUtil.currentUserDetails().set(customerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    setInProgress(false);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        } else {

            executorModel.setUserId(FirebaseUtil.currentUserId());
            executorModel.setCreatedTimestamp(Timestamp.now());

            Uri selectedImageUri = Uri.parse(executorModel.getImageUrl());

            FirebaseUtil.getCurrentLogoPicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        FirebaseUtil.currentCompanyDetails().set(executorModel).addOnCompleteListener(task1 -> {
                            setInProgress(false);
                            if (task1.isSuccessful()) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
    }
}
package com.example.registerotp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentEnterSmsBinding;
import com.example.registerotp.model.CustomerModel;
import com.example.registerotp.utils.AndroidUtil;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginCustomerFragment extends Fragment {

    private FragmentEnterSmsBinding b;
    private NavController navController;
    private CustomerModel customerModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentEnterSmsBinding.inflate(inflater, container, false);
        return b.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        customerModel = new CustomerModel();

        navController = Navigation.findNavController(view);
        /*b.loginCountrycode.registerCarrierNumberEditText(b.etPhone);

        b.btnContinue.setOnClickListener(v -> {
            if (isValid()) {
                customerModel.setUsername(b.etName.getText().toString());
                customerModel.setPassword(b.etPassword.getText().toString());
                customerModel.setPhone(b.loginCountrycode.getFullNumberWithPlus());

                Bundle args = new Bundle();
                args.putParcelable("customerModel", customerModel);
                navController.navigate(R.id.action_loginCustomerFragment_to_OTPFragment, args);
            }
        });
    }

    private boolean isValid() {
        if (b.etName.getText().toString().equals("") || b.etName.getText().toString().length() < 5) {
            b.etName.setError(getString(R.string.invalid_name));
            return false;
        }
        if (!b.loginCountrycode.isValidFullNumber()) {
            b.etPhone.setError(getString(R.string.invalid_phone));
            return false;
        }
        if (b.etPassword.getText().toString().equals("") || b.etName.getText().toString().length() < 5) {
            b.etPassword.setError(getString(R.string.invalid_password));
            return false;
        }
        return true;*/
    }
}
package com.example.registerotp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentLoginExecutorBinding;
import com.example.registerotp.model.ExecutorModel;
import com.example.registerotp.utils.AndroidUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class LoginExecutorFragment extends Fragment {
    private FragmentLoginExecutorBinding b;
    private NavController navController;
    private ExecutorModel executorModel;
    private ActivityResultLauncher<Intent> imagePickLauncher;
    private Uri selectedImageUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(), selectedImageUri, b.btnAddImage);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentLoginExecutorBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executorModel = new ExecutorModel();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        navController = Navigation.findNavController(view);
        b.loginCountrycode.registerCarrierNumberEditText(b.etPhone);

        getCategories();

        b.btnAddImage.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

        b.btnContinue.setOnClickListener(v -> {
            if (isValid()) {
                executorModel.setCompanyName(b.etCompanyName.getText().toString());
                executorModel.setPhone(b.loginCountrycode.getFullNumberWithPlus());
                executorModel.setExperience(Integer.parseInt(b.etExperience.getText().toString()));
                executorModel.setWebLink(b.etLink.getText().toString());
                executorModel.setServiceCategory(b.autoCompleteTextView.getText().toString());
                executorModel.setServiceRadius(Integer.parseInt(b.etServiceRadius.getText().toString()));
                executorModel.setContactInfo(b.etContactInfo.getText().toString());
                executorModel.setAboutMe(b.etAboutMe.getText().toString());
                executorModel.setLegalRepresentation(b.etLegalRepresentation.getText().toString());
                String imageUrl = selectedImageUri.toString();
                executorModel.setImageUrl(imageUrl);


                Bundle args = new Bundle();
                args.putParcelable("executorModel", executorModel);


                //navController.navigate(R.id.action_loginExecutorFragment_to_OTPFragment, args);
            }
        });
    }

    private boolean isValid() {
        if (b.etCompanyName.getText().toString().equals("") || b.etCompanyName.getText().toString().length() < 3) {
            b.etCompanyName.setError(getString(R.string.invalid_comp_name));
            return false;
        }
        if (!b.loginCountrycode.isValidFullNumber()) {
            b.etPhone.setError(getString(R.string.invalid_phone));
            return false;
        }
        if (b.etExperience.getText().toString().equals("") || b.etExperience.getText().toString().length() < 1) {
            b.etExperience.setError(getString(R.string.invalid_experience));
            return false;
        }
        if (b.etLink.getText().toString().equals("") || b.etLink.getText().toString().length() < 5) {
            b.etLink.setError(getString(R.string.field_must_be_not_empty));
            return false;
        }
        if (b.etServiceRadius.getText().toString().equals("") || b.etServiceRadius.getText().toString().length() < 2) {
            b.etServiceRadius.setError(getString(R.string.field_must_be_not_empty));
            return false;
        }
        if (b.etContactInfo.getText().toString().equals("") || b.etContactInfo.getText().toString().length() < 5) {
            b.etContactInfo.setError(getString(R.string.field_must_be_not_empty));
            return false;
        }
        if (b.etAboutMe.getText().toString().equals("") || b.etAboutMe.getText().toString().length() < 5) {
            b.etAboutMe.setError(getString(R.string.field_must_be_not_empty));
            return false;
        }
        if (b.etLegalRepresentation.getText().toString().equals("") || b.etLegalRepresentation.getText().toString().length() < 5) {
            b.etLegalRepresentation.setError(getString(R.string.field_must_be_not_empty));
            return false;
        }
        if (selectedImageUri == null) {
            AndroidUtil.showToast(requireActivity(), getString(R.string.choose_image));
            return false;
        }
        return true;
    }

    private void getCategories() {
        FirebaseFirestore.getInstance().collection("services")
                .get().addOnSuccessListener(result -> {
                    for (QueryDocumentSnapshot a : result) {
                        List<String> professions = (List<String>) a.getData().get("categories");
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.drop_down_item, professions);
                        b.autoCompleteTextView.setAdapter(arrayAdapter);
                    }
                });
    }

    @Override
    public void onResume() {
        if (selectedImageUri != null){
            AndroidUtil.setProfilePic(getContext(), selectedImageUri, b.btnAddImage);
        }
        super.onResume();
    }
}
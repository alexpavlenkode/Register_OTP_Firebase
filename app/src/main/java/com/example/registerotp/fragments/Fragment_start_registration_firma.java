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

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentStartRegistrationBinding;
import com.example.registerotp.databinding.FragmentStartRegistrationFirmaBinding;
import com.example.registerotp.model.FirmenModel;
import com.example.registerotp.utils.AndroidUtil;
import com.google.firebase.auth.FirebaseAuth;


public class Fragment_start_registration_firma extends Fragment {
    private FirmenModel firmenmodell;
    private FragmentStartRegistrationFirmaBinding binding;
    private FirebaseAuth mAuth;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        firmenmodell = bundle.getParcelable("firmenModell");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Раздуваем макет
        //Вставляем полученные данные
        binding = FragmentStartRegistrationFirmaBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        binding.eingabefirmenName.setText(firmenmodell.getCompanyName());
        binding.inputPhoneNumber.setText(firmenmodell.getPhone());
        if(isValid()){
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.regestrierenWeiterBtn.setOnClickListener(clickedView ->{
            if(isValid()){
                if(binding.checkBoxContackt.isChecked()){
                    firmenmodell.setcontacktEnable(true);
                }else{
                    firmenmodell.setcontacktEnable(false);
                }
                String mail = binding.eMail.getEditText().getText().toString();
                firmenmodell.seteMail(mail);
                Bundle args = new Bundle();
                args.putParcelable("firmenModell",firmenmodell);
                navController.navigate(R.id.id_action_to_set_keywords, args);
            }
        });


    }

    private boolean isValid(){
        String mail = binding.eingabeMail.getText().toString();
        String firmenName = binding.eingabefirmenName.getText().toString();
        String phone = binding.inputPhoneNumber.getText().toString();

        String vorUndNachName = binding.eingabefirmenVorUndNachName.getText().toString();

        if (!isValidEmail(mail)) {
            binding.eMail.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_email_name));
            return false;
        }
        if (firmenName.isEmpty()) {
            binding.firmenName.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.empty_firmen_name));
            return false;
        }
        if (phone.isEmpty()) {
            binding.inputPhoneNumber.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_telefon));
            return false;
        }
        if (vorUndNachName.isEmpty()) {
            binding.firmenVorUndNachName.setError("");
            AndroidUtil.showToast(getActivity(), getString(R.string.invalid_firmen_name));
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email != null && email.matches(emailPattern);
    }
}
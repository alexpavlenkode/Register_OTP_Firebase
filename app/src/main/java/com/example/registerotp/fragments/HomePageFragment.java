package com.example.registerotp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.databinding.FragmentHomepageBinding;
import com.example.registerotp.model.KundenModell;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageFragment extends Fragment {
    private FragmentHomepageBinding binding;
    private NavController navController;
    private KundenModell kundenModell;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String createdTimestamp,email,loginName,phone,userId,username,regComplet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(uid);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            createdTimestamp = document.getString("createdTimestamp");
                            email = document.getString("email");
                            loginName = document.getString("loginName");
                            phone = document.getString("phone");
                            regComplet = document.getBoolean("regComplet").toString();
                            userId = document.getString("userId");
                            username = document.getString("username");

                            binding.createdTimestamp.setText(createdTimestamp);
                            binding.email.setText(email);
                            binding.loginname.setText(loginName);
                            binding.phone.setText(phone);
                            binding.regComplet.setText(regComplet);
                            binding.userId.setText(userId);
                            binding.username.setText(username);

                        }
                    }
                }
            });
        }



            /*(documentSnapshot.exists()){
                String createdTimestamp = documentSnapshot.getString("createdTimestamp");
                String email = documentSnapshot.getString("email");
                String loginName = documentSnapshot.getString("loginName");
                String phone = documentSnapshot.getString("phone");
                //boolean regComplet = documentSnapshot.getBoolean("regComplet");
                String userId = documentSnapshot.getString("userId");
                String username = documentSnapshot.getString("username");

            }
        });

        //binding.loginname.setText(loginName);
        //binding.phone.setText(phone);
        //binding.userId.setText(userId);
        //binding.username.setText(username);*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Очистка binding при разрушении view
    }

}

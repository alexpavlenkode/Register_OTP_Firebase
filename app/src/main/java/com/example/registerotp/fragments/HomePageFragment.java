package com.example.registerotp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.databinding.FragmentHomepageBinding;
import com.example.common.model.FirmenModel;
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
    private FirmenModel firmenmodell;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String createdTimestamp,email,loginName,phone,userId,username,regComplet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomepageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            findUserInCollection("users", uid);
        }
    }

    private void findUserInCollection(String collection, String uid){
        DocumentReference userRef = db.collection(collection).document(uid);
        // Получение документа пользователя
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (collection.equals("users")) {
                            // Маппинг документа на модель KundenModell
                            KundenModell user = document.toObject(KundenModell.class);
                            updateUIWithKundenData(user);
                        } else if (collection.equals("firma")) {
                            // Маппинг документа на модель FirmenModel
                            FirmenModel company = document.toObject(FirmenModel.class);
                            updateUIWithFirmenData(company);
                        }
                    }else {
                        // Если пользователь не найден в текущей коллекции, проверяем другую коллекцию
                        if (collection.equals("users")) {
                            findUserInCollection("firma", uid);
                        } else {
                            // Обработка случая, когда пользователь не найден ни в одной из коллекций
                            Toast.makeText(getActivity(), "User not found in both collections", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void updateUIWithKundenData(KundenModell user) {
        binding.email.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        binding.loginname.setText(user.getUsername());
        binding.phone.setText(user.getPhone());
        binding.userId.setText(user.getUserId() != null ? user.getUserId() : "N/A");
        binding.username.setText(user.getLoginName());
    }

    private void updateUIWithFirmenData(FirmenModel company) {
        binding.companyName.setText(company.getCompanyName());
        binding.email.setText(company.geteMail() != null ? company.geteMail() : "N/A");
        binding.userId.setText(company.getUserId()!= null ? company.getUserId() : "N/A");
        binding.keywordsProfession.setText(company.getKeywordsProfession().toString());
        // обновление других полей
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Очистка binding при разрушении view
    }

}

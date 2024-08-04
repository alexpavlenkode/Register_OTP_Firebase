package com.example.registerotp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.common.utils.FirestoreHelper;
import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentSplaschBinding;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplaschFragment extends Fragment {

    private FragmentSplaschBinding binding;
    private NavController navController;
    private FirestoreHelper firestoreHelper;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSplaschBinding.inflate(inflater, container, false);


        /*firestoreHelper = new FirestoreHelper();
        List<Map<String, Object>> sampleData = new ArrayList<>();

        // Additional generated tickets
        Map<String, Object> task3 = new HashMap<>();
        task3.put("adresse", "Hauptbahnhof, Leipzig");
        task3.put("date", new Timestamp(new Date()));
        task3.put("description", "Broken air conditioning unit in the waiting hall.");
        task3.put("geopoint", new GeoPoint(51.3455, 12.3810));
        task3.put("profession", "Klimaanlagentechniker");
        task3.put("short_description", "AC unit malfunction.");
        task3.put("status", 0);
        task3.put("title", "AC Repair");
        task3.put("urgency", 1);
        task3.put("userId", "user123");
        task3.put("views", 10);
        sampleData.add(task3);

        Map<String, Object> task4 = new HashMap<>();
        task4.put("adresse", "Leipziger Messe, Leipzig");
        task4.put("date", new Timestamp(new Date()));
        task4.put("description", "Need an electrician to fix lighting issues.");
        task4.put("geopoint", new GeoPoint(51.3958, 12.4036));
        task4.put("profession", "Elektriker");
        task4.put("short_description", "Lighting issues at the fairground.");
        task4.put("status", 1);
        task4.put("title", "Lighting Repair");
        task4.put("urgency", 3);
        task4.put("userId", "user987");
        task4.put("views", 2);
        sampleData.add(task4);

        Map<String, Object> task5 = new HashMap<>();
        task5.put("adresse", "Marktplatz, Leipzig");
        task5.put("date", new Timestamp(new Date()));
        task5.put("description", "Broken escalator in the shopping mall.");
        task5.put("geopoint", new GeoPoint(51.3402, 12.3746));
        task5.put("profession", "Mechaniker");
        task5.put("short_description", "Escalator repair needed.");
        task5.put("status", 0);
        task5.put("title", "Escalator Repair");
        task5.put("urgency", 2);
        task5.put("userId", "user654");
        task5.put("views", 8);
        sampleData.add(task5);

        Map<String, Object> task6 = new HashMap<>();
        task6.put("adresse", "Universitätsstraße, Leipzig");
        task6.put("date", new Timestamp(new Date()));
        task6.put("description", "Painting job required for a new office.");
        task6.put("geopoint", new GeoPoint(51.3386, 12.3801));
        task6.put("profession", "Maler");
        task6.put("short_description", "Office painting.");
        task6.put("status", 1);
        task6.put("title", "Office Painting");
        task6.put("urgency", 2);
        task6.put("userId", "user321");
        task6.put("views", 12);
        sampleData.add(task6);

// Добавляем остальные задачи аналогичным образом

        // Добавление данных в Firestore
        for (Map<String, Object> data : sampleData) {
            firestoreHelper.addTicket(
                    (String) data.get("adresse"),
                    (Timestamp) data.get("date"),
                    (String) data.get("description"),
                    (GeoPoint) data.get("geopoint"),
                    (String) data.get("profession"),
                    (String) data.get("short_description"),
                    (Integer) data.get("status"),
                    (String) data.get("title"),
                    (Integer) data.get("urgency"),
                    (String) data.get("userId"),
                    (Integer) data.get("views")
            );
        }*/


        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(), com.example.companies.CompanyActivity.class);
        //Intent intent = new Intent(getContext(), com.example.users.UserActivity.class);
        startActivity(intent);
        /*navController = Navigation.findNavController(view);
        ImageView sichtLogoText = binding.splaschTextLogo;
        sichtLogoText.setAlpha(0f);

        sichtLogoText.setVisibility(View.VISIBLE);
        sichtLogoText.animate().alpha(1f).setDuration(2500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Handler
                new Handler().postDelayed(() -> {
                    mAuth = FirebaseAuth.getInstance();
                    db = FirebaseFirestore.getInstance();
                    //FirebaseUtil.logout();
                    if (FirebaseUtil.isLoggedIn()) {

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String uid = currentUser.getUid();

                        DocumentReference userRef = db.collection("id").document(uid);
                        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Boolean isPrivatPerson = document.getBoolean("isPrivatPerson");
                                        if (isPrivatPerson) {
                                            //navController.navigate(R.id.id_action_to_home_page_from_splasch);
                                        }
                                        else {
                                            Intent intent = new Intent(getContext(), com.example.companies.CompanyActivity.class);
                                            //Intent intent = new Intent(getContext(), com.example.users.UserActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        // Document does not exist
                                        System.out.println("No such document!");
                                    }
                                } else {
                                    System.err.println("Error getting documents: " + task.getException());
                                }
                            }
                        });

                    } else {
                        navController.navigate(R.id.id_action_registration_or_login_from_splasch);
                    }
                }, 10);
            }
        });*/



    }
}
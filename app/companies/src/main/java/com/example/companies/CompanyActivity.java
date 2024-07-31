package com.example.companies;

import android.os.Bundle;

import com.example.registerotp.fragments.FirestoreHelper;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.companies.databinding.ActivityCompanyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompanyActivity extends AppCompatActivity {
    SharedViewModel viewModel;

    private ActivityCompanyBinding binding;
    private NavController navController;
    private FirestoreHelper firestoreHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        BottomNavigationView navView = findViewById(R.id.nav_view_corp);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_corp, R.id.navigation_nachrichten, R.id.navigation_meldungen, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewCorp, navController);
    }
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
}

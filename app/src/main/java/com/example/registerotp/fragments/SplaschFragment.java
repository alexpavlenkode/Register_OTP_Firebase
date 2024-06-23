package com.example.registerotp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import com.example.registerotp.R;
import com.example.registerotp.databinding.FragmentSplaschBinding;
import com.example.registerotp.utils.FirebaseUtil;

public class SplaschFragment extends Fragment {

    private FragmentSplaschBinding binding;
    private NavController navController;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSplaschBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        ImageView sichtLogoText = binding.splaschTextLogo;
        sichtLogoText.setAlpha(0f);

        sichtLogoText.setVisibility(View.VISIBLE);
        sichtLogoText.animate().alpha(1f).setDuration(2500).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Handler
                new Handler().postDelayed(() -> {
                    //Prüft, ob der Benutzer eingeloggt ist
                    //1. wenn die SMS noch nicht bestätigt wurde
                    //2. Wenn SMS bestätigt aber noch nicht registriert
                    //3. Wenn regestrierung erfolgt wurde
                    FirebaseUtil.logout();
                    if (FirebaseUtil.isLoggedIn()) {
                        navController.navigate(R.id.id_action_to_home_page_from_splasch);
                        //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        /**FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                         checkUserData(user).addOnCompleteListener(task -> {
                         if (task.isSuccessful()) {
                         boolean isDataComplete = task.getResult();
                         if (isDataComplete) {
                         //Пользователь прошёл регисрацию полностью
                         Log.w(StateSet.TAG, "UserRegistrationComplite: True", task.getException());
                         startActivity(new Intent(SplashActivity.this, MainActivity.class));
                         } else {
                         Log.w(StateSet.TAG, "UserRegistrationComplite: False", task.getException());
                         }
                         } else {
                         // Handle error
                         }
                         });**/
                        //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        // Wenn der Benutzer nicht eingeloggt ist, startet die LoginActivity
                    } else {
                        //weiterFirma();
                        navController.navigate(R.id.id_action_registration_or_login_from_splasch);
                    }
                }, 1000);
            }
        });



    }
}
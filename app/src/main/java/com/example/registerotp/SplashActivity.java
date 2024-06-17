package com.example.registerotp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.registerotp.model.KundenModell;
import com.example.registerotp.utils.AndroidUtil;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView sichtLogoText = findViewById(R.id.splasch_text_logo);

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

                    if (FirebaseUtil.isLoggedIn()) {
                        // Wenn der Benutzer eingeloggt ist
                        // 2 und 3 wird geprüft
                        //FirebaseUtil.logout();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String displayName = user.getDisplayName();

                        weiterKunde();
                        //startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        // Wenn der Benutzer nicht eingeloggt ist, startet die LoginActivity
                    } else {
                        weiterFirma();
                        //startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }, 1000);
            }
        });

    }
    private void weiterKunde() {
        // Launch an activity from the workerskunde module
        //startActivity(new Intent(SplashActivity.this, RegActivity.class));
        Intent intent = new Intent(SplashActivity.this, RegActivity.class);
        startActivity(intent);

    }

    private void weiterFirma() {
        // Launch an activity from the workerskunde module
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

}
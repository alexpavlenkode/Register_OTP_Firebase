package com.example.registerotp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.registerotp.fragments.RegFragment;
import com.google.android.material.textfield.TextInputLayout;

/*
 * Aktiviert die Edge-to-Edge-Darstellung und stellt sicher, dass der Inhalt der Aktivität
 * nicht von den Systemleisten verdeckt wird, indem er passende Abstände setzt!
 * */
public class RegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Aktiviert die Edge-to-Edge-Darstellung für diese Activity (dies ist eine eigene Methode)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
    }



}
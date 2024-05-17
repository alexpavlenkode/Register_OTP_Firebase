package com.example.registerotp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.registerotp.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int color_left = ContextCompat.getColor(this, R.color.logo_tet_left);
        int color_right = ContextCompat.getColor(this, R.color.logo_tet_right);

        TextView logo_text = findViewById(R.id.logo_text);
        String get_logo_text = getString(R.string.app_name_s);

        SpannableString spannableString = new SpannableString(get_logo_text);
        spannableString.setSpan(new ForegroundColorSpan(color_left),0,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color_right), get_logo_text.length() - 3, get_logo_text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        logo_text.setText(spannableString);




        new Handler().postDelayed(() -> {
            //Prüft, ob der Benutzer eingeloggt ist
            if (FirebaseUtil.isLoggedIn()) {
                // Wenn der Benutzer eingeloggt ist, startet die MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            // Wenn der Benutzer nicht eingeloggt ist, startet die LoginActivity
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 3000);
    }
}
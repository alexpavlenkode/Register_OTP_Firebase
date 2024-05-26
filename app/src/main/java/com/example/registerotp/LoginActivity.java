package com.example.registerotp;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

/*
* Aktiviert die Edge-to-Edge-Darstellung und stellt sicher, dass der Inhalt der Aktivität
* nicht von den Systemleisten verdeckt wird, indem er passende Abstände setzt!
* */
public class LoginActivity extends AppCompatActivity {
    private LinearLayout mainLayout;
    private TextInputLayout txtUsername;
    private TextInputLayout txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout = findViewById(R.id.main);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);


        EdgeToEdge.enable(this);
        //in activity_login definierte Layout.
        //setContentView(R.layout.activity_login);
        setContentView(R.layout.fragment_login);

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            //wenn Fenster-Insets (z.B. Statusleiste, Navigationsleiste) angewendet werden.
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/



    }
}
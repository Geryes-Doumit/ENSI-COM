package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;


public class Logo_textActivity extends AppCompatActivity {

    // ...

    public void onLogoClick(View view) {
        // Rafraîchir la page
        finish();
        startActivity(getIntent());
    }

    // ...
}


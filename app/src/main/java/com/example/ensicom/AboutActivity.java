package com.example.ensicom;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ensicom.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("À propos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    public void openGithub(View view) {
        String url = "https://github.com/";
        Snackbar.make(view, "Github", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
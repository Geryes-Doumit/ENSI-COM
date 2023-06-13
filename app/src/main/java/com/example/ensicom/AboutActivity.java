package com.example.ensicom;

import android.content.Intent;
import android.net.Uri;
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
    private final String githubURL = "https://github.com/";
    private final String videoURL = "https://firebasestorage.googleapis.com/v0/b/projet-fin-annee-ddbef.appspot.com/o/LCxr0IDDV8o_rickroll.mp4?alt=media&token=31429524-703c-442c-86f5-01fd681d84e1";

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
        //Snackbar.make(view, "Github", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubURL));
        startActivity(browserIntent);
    }
    public void help(View view) {
        //Snackbar.make(view, "Something happens", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Intent easter = new Intent(view.getContext(), VideoPlayer.class);
        easter.putExtra("videoUrl", videoURL);
        view.getContext().startActivity(easter);

    }

}
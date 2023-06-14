package com.example.ensicom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.ensicom.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAboutBinding binding;
    private final String githubURL = "https://github.com/";
    private final String videoURL = "https://firebasestorage.googleapis.com/v0/b/projet-fin-annee-ddbef.appspot.com/o/rickroll.mp4?alt=media&token=20dfd6f9-d4c1-424f-aa60-ec67b7c85cfc";

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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubURL));
        startActivity(browserIntent);
    }
    public void help(View view) {
        Intent easter = new Intent(view.getContext(), VideoPlayer.class);
        easter.putExtra("videoUrl", videoURL);
        view.getContext().startActivity(easter);

    }

}
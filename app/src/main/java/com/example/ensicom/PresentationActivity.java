package com.example.ensicom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class PresentationActivity extends AppCompatActivity {

    private static final int DELAY_MILLIS = 2000; // Délai en millisecondes avant de passer à la page principale

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        LinearLayout presentationLayout = findViewById(R.id.presentation_layout);
        ImageView logoImage = findViewById(R.id.logo_image);

        // Affiche le logo avec une animation de fondu
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(1000); // Durée de l'animation en millisecondes
        fadeInAnimation.setFillAfter(true);
        logoImage.startAnimation(fadeInAnimation);

        // Délai avant de passer à la page principale
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Animation de disparition du logo
                AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
                fadeOutAnimation.setDuration(1000); // Durée de l'animation en millisecondes
                fadeOutAnimation.setFillAfter(true);
                fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Passage à la page principale (activity_main.xml)
                        Intent intent = new Intent(PresentationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // Applique l'animation de fondu à la présentation et au logo
                presentationLayout.startAnimation(fadeOutAnimation);
                logoImage.startAnimation(fadeOutAnimation);
            }
        }, DELAY_MILLIS);
    }
}


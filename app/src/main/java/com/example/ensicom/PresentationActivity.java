package com.example.ensicom;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PresentationActivity extends AppCompatActivity {

    private static final long DELAY_MILLIS = 3000; // Délai en millisecondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        ImageView logoImage = findViewById(R.id.logo_image);
        TextView welcomeMessage = findViewById(R.id.welcome_message);

        // Animation d'apparition du logo et du message
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
        fadeInAnimation.setDuration(2000); // Durée de l'animation en millisecondes

        logoImage.startAnimation(fadeInAnimation);
        welcomeMessage.startAnimation(fadeInAnimation);

        // Rend le logo et le message visibles à la fin de l'animation
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                logoImage.setVisibility(View.VISIBLE);
                welcomeMessage.setVisibility(View.VISIBLE);

                // Programmation de la tâche différée pour masquer le logo et lancer l'activité par défaut
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logoImage.setVisibility(View.INVISIBLE);
                        welcomeMessage.setVisibility(View.INVISIBLE);
                        launchDefaultActivity();
                    }
                }, DELAY_MILLIS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    private void launchDefaultActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Termine l'activité de présentation pour revenir à l'activité par défaut
    }
}

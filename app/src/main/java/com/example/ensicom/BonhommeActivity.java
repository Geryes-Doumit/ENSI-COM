package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BonhommeActivity extends AppCompatActivity {

    private LinearLayout rubriqueLayout;
    private Button profileRubriqueButton;
    private Button settingsRubriqueButton;
    private Button logoutRubriqueButton;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonhomme);

        // Référence aux vues
        rubriqueLayout = findViewById(R.id.rubrique_layout);
        profileRubriqueButton = findViewById(R.id.profile_rubrique_button);
        settingsRubriqueButton = findViewById(R.id.settings_rubrique_button);
        logoutRubriqueButton = findViewById(R.id.logout_rubrique_button);
        drawerLayout = findViewById(R.id.drawer_layout);


        // Définition des actions de clic des boutons
        profileRubriqueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileRubriqueClick();
            }
        });

        settingsRubriqueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingsRubriqueClick();
            }
        });

        logoutRubriqueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutRubriqueClick();
            }
        });
    }

    public void onProfileButtonClick(View view) {
        // Affichage/masquage de la rubrique
        if (rubriqueLayout.getVisibility() == View.VISIBLE) {
            rubriqueLayout.setVisibility(View.GONE);
        } else {
            rubriqueLayout.setVisibility(View.VISIBLE);
        }
    }

    public void onProfileRubriqueClick() {
        // Code à exécuter pour ouvrir le profil de l'utilisateur
        Toast.makeText(this, "Ouvrir le profil", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void onSettingsRubriqueClick() {
        // Logique pour le clic sur la rubrique "Paramètres"
        // Ajoutez votre code ici
    }

    public void onLogoutRubriqueClick() {
        // Code à exécuter pour effectuer la déconnexion
        Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}

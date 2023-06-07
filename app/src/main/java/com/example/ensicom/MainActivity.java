package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        setupNavigationView();

        Button actualitesButton = findViewById(R.id.button_actualites);
        Button evenementsButton = findViewById(R.id.button_evenements);

        actualitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActualitesButtonClick(v);
            }
        });

        evenementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvenementsButtonClick(v);
            }
        });
    }

    public void onActualitesButtonClick(View view) {
        // Code à exécuter lorsque le bouton "Actualités" est cliqué
        Toast.makeText(this, "Actualités", Toast.LENGTH_SHORT).show();
    }

    public void onEvenementsButtonClick(View view) {
        // Code à exécuter lorsque le bouton "Événements" est cliqué
        Toast.makeText(this, "Événements", Toast.LENGTH_SHORT).show();
    }

    public void onLogoClick(View view) {
        // Code à exécuter lorsque le logo est cliqué
        Toast.makeText(this, "Logo ENSICOM", Toast.LENGTH_SHORT).show();
    }

    public void onUserMenuButtonClick(View view) {
        // Code à exécuter lorsque le menu utilisateur est cliqué
        Toast.makeText(this, "Menu utilisateur", Toast.LENGTH_SHORT).show();
    }

    public void onMenuButtonClick(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Gérer les actions en fonction de l'élément de menu sélectionné
                switch (item.getItemId()) {
                    case R.id.menu_option1:
                        // Action pour l'option 1 du menu
                        return true;
                    case R.id.menu_option2:
                        // Action pour l'option 2 du menu
                        return true;
                    // Ajoutez d'autres options de menu au besoin
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_profil) {
                    openProfile();
                    return true;
                } else if (itemId == R.id.menu_parametres) {
                    // Code pour gérer la sélection de "Paramètres"
                    Toast.makeText(MainActivity.this, "Paramètres", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void openProfile() {
        // Code pour ouvrir le profil utilisateur
        Toast.makeText(this, "Profil", Toast.LENGTH_SHORT).show();
    }
}


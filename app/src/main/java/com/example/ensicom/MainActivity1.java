package com.example.ensicom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.navigation.NavigationView;

public class MainActivity1 extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private MainFragment main_fragment = new MainFragment();

    private void showMenuDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_menu, null);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code à exécuter lorsque l'utilisateur appuie sur OK
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_bonhomme, null);

        Button profileButton = dialogView.findViewById(R.id.profile_rubrique_button);
        Button settingsButton = dialogView.findViewById(R.id.settings_rubrique_button);
        Button logoutButton = dialogView.findViewById(R.id.logout_rubrique_button);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Code à exécuter lorsque l'utilisateur appuie sur OK
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileDialog();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSettingsRubriqueClick(v);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutRubriqueClick(v);
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        ImageView menuButton = findViewById(R.id.menu_button);

        drawerLayout = findViewById(R.id.drawer_layout);
        setupNavigationView();
        setNewFragment(main_fragment);

        ImageView profileButton = findViewById(R.id.user_menu_button);
        Button actualitesButton = findViewById(R.id.button_actualites);
        Button evenementsButton = findViewById(R.id.button_evenements);



        actualitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewFragment(main_fragment);
                onActualitesButtonClick(v);
            }
        });

        evenementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEvenementsButtonClick(v);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuDialog();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileDialog();
            }
        });

    }

    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_layout,fragment);
        ft.commit();
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

    public void onProfileRubriqueClick(View view) {
        // Handle profile button click
    }

    public void onSettingsRubriqueClick(View view) {
        // Handle settings button click
    }

    public void onLogoutRubriqueClick(View view) {
        // Handle logout button click
    }


    public void onMenuButtonClick(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Gérer les actions en fonction de l'élément de menu sélectionné
                int itemId = item.getItemId();
                @SuppressLint("DiscouragedApi") int menuOption1Id = getResources().getIdentifier("menu_option1", "id", getPackageName());
                @SuppressLint("DiscouragedApi") int menuOption2Id = getResources().getIdentifier("menu_option2", "id", getPackageName());

                if (itemId == menuOption1Id) {
                    // Action pour l'option 1 du menu
                    return true;
                } else if (itemId == menuOption2Id) {
                    // Action pour l'option 2 du menu
                    return true;
                }
                // Ajoutez d'autres options de menu au besoin
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
                    Toast.makeText(MainActivity1.this, "Paramètres", Toast.LENGTH_SHORT).show();
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
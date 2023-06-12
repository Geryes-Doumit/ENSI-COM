package com.example.ensicom;

import static com.example.ensicom.R.id.fragment_layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity1 extends AppCompatActivity{

    private DrawerLayout drawerLayout;




    /*
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

*/

    BottomNavigationView bottomNavigationView;

    MainFragment homeFragment = new MainFragment();
    EvenementFragment evenementFragment = new EvenementFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(fragment_layout, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.button_actualites) {
                    getSupportFragmentManager().beginTransaction().replace(fragment_layout, homeFragment).commit();
                    return true;
                }
                else if (id == R.id.button_evenements) {
                    getSupportFragmentManager().beginTransaction().replace(fragment_layout, evenementFragment).commit();
                    return true;
                }
                return false;

            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        findViewById(R.id.profil_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id  == R.id.side_profile) {
                Intent pIntent = new Intent(MainActivity1.this, ProfileActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(pIntent);
            } else if (id == R.id.side_settings) {
                Intent sIntent = new Intent(MainActivity1.this, SettingsActivity.class);
                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(sIntent);
            } else if (id == R.id.side_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent lIntent = new Intent(MainActivity1.this, LoginActivity.class);
                lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(lIntent);
                finish();
            }
            return true;
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}

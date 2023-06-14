package com.example.ensicom;

import static com.example.ensicom.R.id.fragment_layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity1 extends AppCompatActivity{

    private DrawerLayout drawerLayout;

    BottomNavigationView bottomNavigationView;

    NavigationView navigationView;

    TextView profileName;

    ImageView profilePicture;
    String profilePictureUrl;

    MainFragment homeFragment = new MainFragment();
    EvenementFragment evenementFragment = new EvenementFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("user").child(currentUserId);

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
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                        profileName = findViewById(R.id.profile_name_side_menu);
                        profileName.setText(name);
                        profilePicture=findViewById(R.id.profile_pic_side_menu);
                        if (dataSnapshot.exists()) {
                            profilePictureUrl = dataSnapshot.child("profilePicture").getValue(String.class);
                            Glide.with(MainActivity1.this)
                                    .load(profilePictureUrl).circleCrop()
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .error(R.drawable.ic_launcher_foreground)
                                    .into(profilePicture);
                            if (profilePictureUrl == null) {
                                Toast.makeText(MainActivity1.this, "Impossible de charger l'image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity1.this, "L'image n'a pas pu être récupérée", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.profil_button).setOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
            profileName = findViewById(R.id.profile_name_side_menu);
            profileName.setText(name);
            profilePicture=findViewById(R.id.profile_pic_side_menu);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        profilePictureUrl = dataSnapshot.child("profilePicture").getValue(String.class);
                        Glide.with(MainActivity1.this)
                                .load(profilePictureUrl).circleCrop()
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(profilePicture);
                        if (profilePictureUrl == null) {
                            Toast.makeText(MainActivity1.this, "Impossible de charger l'image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity1.this, "L'image n'a pas pu être récupérée", Toast.LENGTH_SHORT).show();
                }
            });
        });

       navigationView = findViewById(R.id.nav_view);
       navigationView .setBackgroundColor(getResources().getColor(R.color.white));
       navigationView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
           @Override
           public void onViewAttachedToWindow(@NonNull View view) {
               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           }

           @Override
           public void onViewDetachedFromWindow(@NonNull View view) {


           }
       });


        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user!=null) {
                    if (user.isAdmin()) {
                        navigationView.getMenu().findItem(R.id.side_admin).setVisible(true);
                        navigationView.getMenu().findItem(R.id.side_users).setVisible(true);
                    } else {
                        navigationView.getMenu().findItem(R.id.side_admin).setVisible(false);
                        navigationView.getMenu().findItem(R.id.side_users).setVisible(false);
                    }
                }
            }
        });



        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id  == R.id.side_profile) {
                Intent pIntent = new Intent(MainActivity1.this, ProfileActivity.class);
                pIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pIntent);
            } else if (id == R.id.side_settings) {
                Intent sIntent = new Intent(MainActivity1.this, SettingsActivity.class);
                sIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sIntent);
            } else if (id == R.id.side_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent lIntent = new Intent(MainActivity1.this, LoginActivity.class);
                lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(lIntent);
                finish();
            } else if (id == R.id.side_about) {
                Intent aIntent = new Intent(MainActivity1.this, AboutActivity.class);
                aIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(aIntent);
            } else if (id == R.id.side_users) {
                Intent uIntent = new Intent(MainActivity1.this, UsersActivity.class);
                uIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(uIntent);
            } else if (id == R.id.side_admin) {
                Intent abIntent = new Intent(MainActivity1.this, ModerationActivity.class);
                abIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(abIntent);
            }
            item.setChecked(false);
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

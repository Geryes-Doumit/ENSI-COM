package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EvenementActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;
    private DrawerLayout drawerLayout;

    public void onActualitesButtonClick(View view) {
        Intent intent = new Intent(EvenementActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onEvenementsButtonClick(View view) {
        // Code à exécuter lorsque le bouton "Actualités" est cliqué
        Toast.makeText(this, "Evenements", Toast.LENGTH_SHORT).show();
    }

    public void onLogoClick(View view) {
        // Code à exécuter lorsque le logo est cliqué
        Toast.makeText(this, "Logo ENSICOM", Toast.LENGTH_SHORT).show();
    }

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

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evenements);
        initWidgets();
        selectedDate = Calendar.getInstance();
        setMonthView();

        ImageView menuButton = findViewById(R.id.menu_button);

        drawerLayout = findViewById(R.id.drawer_layout);
        setupNavigationView();

        ImageView profileButton = findViewById(R.id.user_menu_button);
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
                    Toast.makeText(EvenementActivity.this, "Paramètres", Toast.LENGTH_SHORT).show();
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


    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        Calendar currentDate = Calendar.getInstance();
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(Calendar calendar) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar firstDayOfMonth = Calendar.getInstance();
        firstDayOfMonth.set(year, month, 1);
        int dayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);

        // Décaler le jour de la semaine au lundi (valeur 2)
        dayOfWeek = (dayOfWeek + 5) % 7; // Formule pour obtenir le décalage correct

        // Ajouter les jours du mois au tableau
        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthArray.add(String.valueOf(i));
        }

        // Compléter le tableau avec des chaînes vides pour les jours avant le premier du mois
        for (int i = 0; i < dayOfWeek; i++) {
            daysInMonthArray.add(0, ""); // Ajouter au début de la liste
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public void previousMonthAction(View view)
    {
        selectedDate.add(Calendar.MONTH, -1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate.add(Calendar.MONTH, 1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}






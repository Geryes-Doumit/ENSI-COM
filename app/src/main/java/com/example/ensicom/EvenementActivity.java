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
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jakewharton.threetenabp.AndroidThreeTen;

import com.google.android.material.navigation.NavigationView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.YearMonth;
import org.threeten.bp.format.DateTimeFormatter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.time.LocalDate;

public class EvenementActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private DrawerLayout drawerLayout;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    private FrameLayout popupContainer;
    private TextView popupDateText;
    private RecyclerView popupRecyclerView;
    private Button popupAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_evenements);
        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        ImageView menuButton = findViewById(R.id.menu_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        setupNavigationView();

        ImageView profileButton = findViewById(R.id.user_menu_button);
        Button actualitesButton = findViewById(R.id.button_actualites);
        Button evenementsButton = findViewById(R.id.button_evenements);

        // Initialisez les vues du pop-up
        popupContainer = findViewById(R.id.popupContainer);
        popupDateText = findViewById(R.id.popupDateText);
        popupRecyclerView = findViewById(R.id.popupRecyclerView);
        popupAddButton = findViewById(R.id.popupAddButton);
        popupAddButton.setOnClickListener(this);

        // Créez votre boîte de dialogue avec le bouton "OK" qui ne fait rien
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Erreur");
        builder.setMessage("La requête a été annulée ou a échoué.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ne rien faire
            }
        });
        AlertDialog dialog = builder.create();

        // Affichez la boîte de dialogue lorsqu'il y a une erreur
        if (erreur) {
            dialog.show();
        }

        actualitesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rediriger vers la page ActualitesActivity
                Intent intent = new Intent(EvenementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        evenementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restez sur la même page (EvenementActivity)
                Toast.makeText(EvenementActivity.this, " Événements", Toast.LENGTH_SHORT).show();
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

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);

        int daysInMonth = yearMonth.lengthOfMonth();
        int dayOfWeek = date.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                String day = String.valueOf(i - dayOfWeek);
                // Check if the day has an event
                if (hasEvent(date.withDayOfMonth(Integer.parseInt(day)))) {
                    day = String.format(Locale.getDefault(), "[ %s ]", day); // Enclose the day in brackets
                }
                daysInMonthArray.add(day);
            }
        }
        return daysInMonthArray;
    }

    private void hasEvent(LocalDate date) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        Query query = eventsRef.orderByChild("date").equalTo(date.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Event found for the given date
                    for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        String title = eventSnapshot.child("title").getValue(String.class);
                        // Perform necessary actions with the event data
                        // For example, display the event title or handle it accordingly
                        Toast.makeText(EvenementActivity.this, "Event found: " + title, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No event found for the given date
                    Toast.makeText(EvenementActivity.this, "No event found for the date", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Affichez un message d'erreur dans une boîte de dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(EvenementActivity.this);
                builder.setTitle("Erreur");
                builder.setMessage("La requête a été annulée ou a échoué : " + databaseError.getMessage());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code à exécuter lorsque l'utilisateur appuie sur OK
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            showPopup(dayText);
        }
    }

    private void showPopup(String date) {
        // Définissez la date dans le TextView du pop-up
        popupDateText.setText(date);

        // Récupérez les événements pour la date sélectionnée à partir de votre base de données ou de votre source de données

        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        Query query = eventsRef.orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // La requête a réussi, vous pouvez maintenant traiter les données des événements

                // Initialisez une liste pour stocker les événements
                List<Event> events = new ArrayList<>();

                // Parcourez les données des événements
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Obtenez les détails de l'événement
                    Event event = snapshot.getValue(Event.class);
                    // Ajoutez l'événement à la liste
                    events.add(event);
                }

                // Initialisez l'adaptateur avec les données des événements
                EventAdapter eventAdapter = new EventAdapter(events);

                // Configurez le RecyclerView avec l'adaptateur
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                popupRecyclerView.setLayoutManager(layoutManager);
                popupRecyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // La requête a été annulée ou a échoué, vous pouvez gérer cette situation ici si nécessaire

                // Affichez un message d'erreur dans une boîte de dialogue
                AlertDialog.Builder builder = new AlertDialog.Builder(EvenementActivity.this);
                builder.setTitle("Erreur");
                builder.setMessage("La requête a été annulée ou a échoué.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code à exécuter lorsque l'utilisateur appuie sur OK

                        resetFields();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        });

        // Affichez le pop-up
        popupContainer.setVisibility(View.VISIBLE);
    }

    private void addEvent() {
        // Créez une boîte de dialogue ou une autre activité pour saisir les détails de l'événement
        AlertDialog.Builder builder = new AlertDialog.Builder(EvenementActivity.this);
        builder.setTitle("Ajouter un événement");

        // Créez les champs de saisie pour les détails de l'événement
        EditText titleEditText = new EditText(EvenementActivity.this);
        titleEditText.setHint("Titre de l'événement");
        builder.setView(titleEditText);

        // Définissez les boutons "Ajouter" et "Annuler"
        builder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String eventTitle = titleEditText.getText().toString();

                // Vérifiez si les détails de l'événement sont valides
                if (eventTitle.isEmpty()) {
                    Toast.makeText(EvenementActivity.this, "Veuillez saisir un titre pour l'événement", Toast.LENGTH_SHORT).show();
                } else {
                    // Obtenez une référence à la base de données
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    // Référence à la collection "events"
                    DatabaseReference eventsRef = database.getReference("events");

                    // Créez un nouvel identifiant unique pour l'événement
                    String eventId = eventsRef.push().getKey();

                    // Créez un objet Event avec les détails de l'événement
                    Event event = new Event(eventId, eventTitle, eventDescription, eventDate);

                    event.setTitle(eventTitle);

                    // Ajoutez l'événement à votre base de données ou source de données
                    eventsRef.child(eventId).setValue(event)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EvenementActivity.this, "Événement ajouté avec succès", Toast.LENGTH_SHORT).show();

                                    // Rafraîchissez la liste des événements dans le pop-up
                                    refreshEventList();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Une erreur s'est produite lors de l'ajout de l'événement à la base de données
                                    Toast.makeText(EvenementActivity.this, "Erreur lors de l'ajout de l'événement", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Ne rien faire, l'utilisateur a annulé l'ajout de l'événement
            }
        });

        // Affichez la boîte de dialogue pour ajouter l'événement
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void refreshEventList() {
        // Récupérez les événements pour la date sélectionnée à partir de votre base de données ou de votre source de données
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Event> eventList = new ArrayList<>();

                // Parcourez les données des événements
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Obtenez les détails de chaque événement
                    Event event = eventSnapshot.getValue(Event.class);
                    // Ajoutez l'événement à la liste
                    eventList.add(event);
                }

                // Initialisez l'adaptateur avec les données des événements
                EventAdapter eventAdapter = new EventAdapter(eventList);

                // Configurez le RecyclerView pour afficher la liste des événements
                LinearLayoutManager layoutManager = new LinearLayoutManager(EvenementActivity.this);
                eventRecyclerView.setLayoutManager(layoutManager);
                eventRecyclerView.setAdapter(eventAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // La requête a été annulée ou a échoué, vous pouvez gérer cette situation ici si nécessaire
                Toast.makeText(EvenementActivity.this, "Erreur lors du chargement des événements", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.popupAddButton) {
            // Gérez le clic sur le bouton "+" dans le pop-up
            addEvent();
        }
    }

    public void onActualitesButtonClick(View view) {
        // Code à exécuter lorsque le bouton "Actualités" est cliqué
        Toast.makeText(this, "Actualités", Toast.LENGTH_SHORT).show();
    }

    public void onEvenementsButtonClick(View view) {
        Intent intent = new Intent(EvenementActivity.this, EvenementActivity.class);
        startActivity(intent);
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
}


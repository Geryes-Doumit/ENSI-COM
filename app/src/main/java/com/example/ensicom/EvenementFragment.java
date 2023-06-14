package com.example.ensicom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.Toast;
import java.util.ArrayList;


public class EvenementFragment extends Fragment implements CalendarAdapter.OnItemListener, View.OnClickListener {
    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;
    private FloatingActionButton addEventButton;
    private AlertDialog alertDialog;
    private DatabaseReference eventsRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedDate = Calendar.getInstance();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_evenement, container, false);

        addEventButton = view.findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewEventActivity.class);
            startActivity(intent);
        });

        // Find the next month button and set the OnClickListener
        Button nextMonthButton = view.findViewById(R.id.nextMonthAction);
        nextMonthButton.setOnClickListener(this);

        // Find the next month button and set the OnClickListener
        Button previousMonthButton = view.findViewById(R.id.previousMonthAction);
        previousMonthButton.setOnClickListener(this);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);

        // Initialize Firebase Realtime Database reference
        eventsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");

        setMonthView();
        getEventsList();
        return view;
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
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

    public void previousMonthAction(View view) {
        selectedDate.add(Calendar.MONTH, -1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate.add(Calendar.MONTH, 1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String date = dayText + " " + monthYearFromDate(selectedDate);
            getEvents(date);
        }
    }

    public ArrayList<String> getEventsList() {
        eventsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

            }
        });
        return null;
    }

    public void getEvents(String date) {
        //DatabaseReference eventsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("events");

        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<EventPost> eventsList = new ArrayList<>();

                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    String year = yearSnapshot.getKey();
                    Toast.makeText(view.getContext(), year, Toast.LENGTH_SHORT);

//                    for (DataSnapshot monthSnapshot : yearSnapshot.getChildren()) {
//                        String month = monthSnapshot.getKey();
//
//                        for (DataSnapshot daySnapshot : monthSnapshot.getChildren()) {
//                            for (DataSnapshot eventSnapshot : daySnapshot.getChildren()) {
//                                String eventId = eventSnapshot.getKey();
//                                EventPost eventPost = eventSnapshot.getValue(EventPost.class);
//                                eventsList.add(eventPost);
//                            }
//                        }
//                    }
                }

                // Filtrer les événements en fonction de la date sélectionnée
                ArrayList<EventPost> filteredEventsList = filterEventsByDate(eventsList, date);

                showEventDialog(filteredEventsList); // Afficher les événements dans la boîte de dialogue
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Gérer les erreurs
            }
        });
    }

    private ArrayList<EventPost> filterEventsByDate(ArrayList<EventPost> eventsList, String date) {
        ArrayList<EventPost> filteredEventsList = new ArrayList<>();

        for (EventPost event : eventsList) {
            if (event.getEventDate().equals(date)) {
                filteredEventsList.add(event);
            }
        }

        return filteredEventsList;
    }

    private void showEventDialog(ArrayList<EventPost> eventsList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        View showView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_event_list, null);

        RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(getContext(), eventsList);
        recyclerView.setAdapter(eventRecyclerAdapter);

        builder.setView(showView);
        alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextMonthAction) {
            nextMonthAction();
        } else if (v.getId() == R.id.previousMonthAction) {
            previousMonthAction();
        }
    }

    private void nextMonthAction() {
        selectedDate.add(Calendar.MONTH, 1);
        setMonthView();
    }

    private void previousMonthAction() {
        selectedDate.add(Calendar.MONTH, -1);
        setMonthView();
    }
}

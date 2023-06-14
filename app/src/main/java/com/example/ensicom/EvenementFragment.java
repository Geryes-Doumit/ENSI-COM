package com.example.ensicom;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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

    private List<EventPost> eventsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedDate = Calendar.getInstance();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_evenement, container, false);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        addEventButton = view.findViewById(R.id.addEventButton);

        calendarRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        calendarRecyclerView.setAdapter(new CalendarAdapter(daysInMonthArray(selectedDate), EvenementFragment.this, eventsList));

        monthYearText.setText(monthYearFromDate(selectedDate));

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

        getEventsList(selectedDate);
        return view;
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

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String date = dayText + " " + monthYearFromDate(selectedDate);
            getEventsList(selectedDate);
        }
    }

    public void getEventsList(Calendar calendar) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        String year = yearFormat.format(calendar.getTime());
        String month = monthFormat.format(calendar.getTime());
        if (month.charAt(0) == '0') {
            month = String.valueOf(month.charAt(1));
        }
        eventsList.clear();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("events").child(year).child(month);

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                } else {

                    for (DataSnapshot daySnapshot : task.getResult().getChildren()) {
                        int eventCount = (int) daySnapshot.getChildrenCount();
                        Toast.makeText(view.getContext(), String.valueOf(eventCount), Toast.LENGTH_SHORT).show();

                        for (DataSnapshot eventSnapshot : daySnapshot.getChildren()) {
                            EventPost event = eventSnapshot.getValue(EventPost.class);
                            Toast.makeText(view.getContext(), event.getEventName(), Toast.LENGTH_SHORT).show();
                            eventsList.add(event);
                            calendarRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private ArrayList<EventPost> filterEventsByDate(ArrayList<EventPost> eventsList, String date) {
        ArrayList<EventPost> filteredEventsList = new ArrayList<>();

        for (EventPost event : eventsList) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            String dateEvent = dateFormat.format(event.getEventDate());
            if (event.getEventDate().equals(dateEvent)) {
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
        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(eventsList);
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
        getEventsList(selectedDate);
        //setMonthView();
    }

    private void previousMonthAction() {
        selectedDate.add(Calendar.MONTH, -1);
        getEventsList(selectedDate);
        //setMonthView();
    }
}


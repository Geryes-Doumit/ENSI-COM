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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.widget.Toast;
import java.util.ArrayList;


public class EvenementFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;
    private FloatingActionButton addEventButton;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<EventPost> eventsList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedDate = Calendar.getInstance();
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_evenement, container, false);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        addEventButton = view.findViewById(R.id.addEventButton);
        addEventButton.setVisibility(View.GONE);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("user").child(currentUserId);

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user!=null) {
                    if (user.isAdmin()) {
                        addEventButton.setVisibility(View.VISIBLE);
                    } else {
                        addEventButton.setVisibility(View.GONE);
                    }
                }
            }
        });

        calendarRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 7));
        calendarRecyclerView.setAdapter(new CalendarAdapter(daysInMonthArray(selectedDate), eventsList));

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

        swipeRefreshLayout = view.findViewById(R.id.homeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getEventsList(selectedDate);
            swipeRefreshLayout.setRefreshing(false);
        });
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

//    @Override
//    public void onItemClick(int position, String dayText) {
//        if (!dayText.equals("")) {
//            String date = dayText + " " + monthYearFromDate(selectedDate);
//        }
//    }

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

                        for (DataSnapshot eventSnapshot : daySnapshot.getChildren()) {
                            EventPost event = eventSnapshot.getValue(EventPost.class);
                            eventsList.add(event);
                            calendarRecyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, eventsList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
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
        setMonthView();
    }

    private void previousMonthAction() {
        selectedDate.add(Calendar.MONTH, -1);
        getEventsList(selectedDate);
        setMonthView();
    }
}


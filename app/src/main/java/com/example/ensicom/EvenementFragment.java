package com.example.ensicom;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.Toast;
import java.util.ArrayList;


public class EvenementFragment extends Fragment {
    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;
    private FloatingActionButton addEventButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_evenement, container, false);

        addEventButton = view.findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewEventActivity.class);
            startActivity(intent);
        });

        initWidgets();

        //setMonthView();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDate = Calendar.getInstance();
    }




    private void initWidgets() {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }
/*
    private void setMonthView() {
        Calendar currentDate = Calendar.getInstance();
        monthYearText.setText(monthYearFromDate(selectedDate));

        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, (CalendarAdapter.OnItemListener) this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    } */

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
/*
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

    */

    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

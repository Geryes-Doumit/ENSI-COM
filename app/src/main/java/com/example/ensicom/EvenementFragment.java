package com.example.ensicom;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
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

public class EvenementFragment extends Fragment {

    private View view;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;
    private DrawerLayout drawerLayout;
    private FloatingActionButton addEventButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_evenement, container, false);

        addEventButton = view.findViewById(R.id.addEventButton);

        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewEventActivity.class);
            startActivity(intent);
        });
        return view;
    }
}
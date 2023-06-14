package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private List<EventPost> eventsList;
    private Calendar selectedDate;
    private SimpleDateFormat dayFormat;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, List<EventPost> eventsList) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayOfMonth = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayOfMonth);

        Toast.makeText(holder.itemView.getContext(), String.valueOf(eventsList.size()), Toast.LENGTH_SHORT).show();

        for (EventPost event : eventsList) {
            String[] date = event.getEventDate().split("/");
            String day = date[2];
            String inPosition = holder.dayOfMonth.getText().toString();
            // Obtenez le nombre d'événements pour cette date
            int eventCount = getEventCount(String.valueOf(event.getEventDate()));
            if (inPosition.equals(day)) {
                holder.eventOfDay.setText(String.valueOf(eventCount));
            }
        }
    }

    private String monthYearFromDate(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private int getEventCount(String date) {
        int count = 0;
        for (EventPost event : eventsList) {
            if (event.getEventDate().equals(date)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}





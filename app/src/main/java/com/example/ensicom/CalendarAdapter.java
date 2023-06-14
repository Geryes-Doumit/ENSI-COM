package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;
    private List<EventPost> eventsList;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, List<EventPost> eventsList)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.eventOfDay.setText("");
        for (int i = 0; i < eventsList.size(); i++) {
            String[] date = eventsList.get(i).getEventDate().split("/");
            String day = date[2];
            String inpos = holder.dayOfMonth.getText().toString();
            if (inpos.equals(day)) {
                holder.eventOfDay.setText(eventsList.get(i).getEventName());
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}




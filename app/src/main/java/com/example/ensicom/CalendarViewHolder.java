package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;

public class CalendarViewHolder extends RecyclerView.ViewHolder
{
    public final TextView dayOfMonth;
    public final TextView eventOfDay;

    public final LinearLayout eventCell;
    public TextView eventTextView;
    public CalendarViewHolder(@NonNull View itemView)
    {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);
        eventOfDay = itemView.findViewById(R.id.cellEventNumber);
        eventCell = itemView.findViewById(R.id.eventCell);
    }
}



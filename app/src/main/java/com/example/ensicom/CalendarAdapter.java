package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    // Méthode pour créer une nouvelle instance de ViewHolder
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    // Méthode pour lier les données à la vue de l'élément de la liste
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String dayText = daysList.get(position);

        if (!dayText.equals("")) {
            holder.dayTextView.setText(dayText);

            // Customize the appearance for days with events
            if (dayText.startsWith("[") && dayText.endsWith("]")) {
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.event_day_text_color));
                holder.dayTextView.setBackgroundResource(R.drawable.event_day_background);
            } else {
                // Reset the appearance for other days
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.default_day_text_color));
                holder.dayTextView.setBackgroundResource(0);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle item click
                    if (onItemListener != null) {
                        onItemListener.onItemClick(position, dayText);
                    }
                }
            });
        } else {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    // Méthode pour obtenir le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    // Interface pour gérer les clics sur les éléments de la liste
    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}


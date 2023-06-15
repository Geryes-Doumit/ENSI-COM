package com.example.ensicom;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private List<EventPost> eventsList;
    private Calendar selectedDate;
    private SimpleDateFormat dayFormat;

    public CalendarAdapter(ArrayList<String> daysOfMonth, List<EventPost> eventsList) {
        this.daysOfMonth = daysOfMonth;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        String dayOfMonth = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayOfMonth);

        for (EventPost event : eventsList) {
            String[] date = event.getEventDate().split("/");
            String day = date[2];
            String inPosition = holder.dayOfMonth.getText().toString();
            // Obtenez le nombre d'événements pour cette date
            if (inPosition.equals(day)) {
                int eventCount = getEventCount(String.valueOf(event.getEventDate()));
                holder.eventOfDay.setText(String.valueOf(eventCount));

                holder.dayOfMonth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ShowEventActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("eventDate", event.getEventDate());
                        v.getContext().startActivity(intent);
                    }
                });
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

    private ArrayList<EventPost> filterEventsByDate(CalendarViewHolder holder) {
        ArrayList<EventPost> filteredEventsList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat datesFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String year = yearFormat.format(calendar.getTime());
        String month = monthFormat.format(calendar.getTime());
        String dates = datesFormat.format(calendar.getTime());
        Toast.makeText(holder.dayOfMonth.getContext(), dates, Toast.LENGTH_SHORT).show();

        if (month.charAt(0) == '0') {
            month = String.valueOf(month.charAt(1));
        }
        eventsList.clear();
        DatabaseReference showref = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("events").child(year).child(month).child(dates);

        showref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(holder.dayOfMonth.getContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                } else {
                    filteredEventsList.clear();
                    for (DataSnapshot eventSnapshot : task.getResult().getChildren()) {
                        EventPost event = eventSnapshot.getValue(EventPost.class);
                        filteredEventsList.add(event);
                        Toast.makeText(holder.dayOfMonth.getContext(), event.getEventName(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (!filteredEventsList.isEmpty()) {
                    // Afficher la boîte de dialogue avec les événements filtrés
                    showEventDialog(holder, filteredEventsList);
                }
                else {
                    Toast.makeText(holder.dayOfMonth.getContext(), "Aucun évènement trouvé.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return filteredEventsList;
    }

    private void showEventDialog(CalendarViewHolder holder, ArrayList<EventPost> eventsList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.dayOfMonth.getContext());
        builder.setCancelable(true);
        View showView = LayoutInflater.from(holder.dayOfMonth.getContext()).inflate(R.layout.dialog_event_list, null);
        RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.dayOfMonth.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(eventsList);
        recyclerView.setAdapter(eventRecyclerAdapter);
        builder.setView(showView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}





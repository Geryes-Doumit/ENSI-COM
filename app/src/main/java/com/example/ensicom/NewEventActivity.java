package com.example.ensicom;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity {

    private EditText eventTitleView;
    private EditText eventDescriptionView;
    private TextView eventDateView;
    private ImageView dateButton;
    private EditText eventLocationView;
    private Button postEventButton;

    private DatabaseReference eventsRef;
    private ArrayList<EventPost> eventList;
    private EventRecyclerAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        eventTitleView = findViewById(R.id.eventTitleEditText);
        eventDescriptionView = findViewById(R.id.eventDescriptionEditText);
        eventDateView = findViewById(R.id.dateTextView);
        eventLocationView = findViewById(R.id.locationEditText);
        dateButton = findViewById(R.id.calendarImageView);
        postEventButton = findViewById(R.id.postEvent);
        eventList = new ArrayList<>();
        eventAdapter = new EventRecyclerAdapter(this, eventList);
        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        eventsRef = database.getReference("events");

        postEventButton.setOnClickListener(v -> {
            String eventTitle = eventTitleView.getText().toString();
            String eventDescription = eventDescriptionView.getText().toString();
            String eventDate = eventDateView.getText().toString();
            String eventLocation = eventLocationView.getText().toString();

            if (TextUtils.isEmpty(eventTitle) || TextUtils.isEmpty(eventDescription) || TextUtils.isEmpty(eventDate) || TextUtils.isEmpty(eventLocation)) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                String[] dateParts = eventDate.split("/");

                if (dateParts.length == 3) {
                    String annee = dateParts[0];
                    String mois = dateParts[1];
                    String jour = dateParts[2];

                    EventPost eventPost = new EventPost("", eventTitle, eventDescription, eventDate, eventLocation);

                    String eventId = eventsRef.child(annee).child(mois).child(jour).push().getKey();
                    eventPost.setEventId(eventId);

                    eventsRef.child(annee).child(mois).child(jour).child(eventId).setValue(eventPost)
                            .addOnFailureListener(e -> {
                                eventsRef.child(annee).child(mois).child(jour).child(eventId).removeValue();
                                Toast.makeText(this, "Erreur lors de l'ajout de l'évènement", Toast.LENGTH_SHORT).show();
                            })
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Evènement ajouté avec succès", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                } else {
                    Toast.makeText(this, "Format de date invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dateButton.setOnClickListener(v -> showCalendar());
        eventDateView.setOnClickListener(v -> showCalendar());
    }

    public void showCalendar() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(NewEventActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = year1 + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                    eventDateView.setText(selectedDate);
                    displayEventsForDate(selectedDate);
                }, year, month, day);
        picker.show();
    }

    public void displayEventsForDate(String date) {
        // Firebase Database reference for events
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("events");

        // Extract year, month, and day from the selected date
        String[] dateParts = date.split("/");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        // Create a query to retrieve events for the selected date
        Query eventsQuery = eventsRef.child(year).child(month).child(day);

        eventsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous event data
                // Assuming you have a list or RecyclerView to display events
                eventList.clear();

                // Iterate through the events data
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    // Parse each event
                    EventPost eventPost = eventSnapshot.getValue(EventPost.class);

                    // Add the event to your list or RecyclerView adapter
                    eventList.add(eventPost);
                }

                // Notify the adapter that the data has changed
                // Assuming you have an adapter for your event list
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the retrieval of events
                Toast.makeText(NewEventActivity.this, "Erreur lors de la récupération des événements", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

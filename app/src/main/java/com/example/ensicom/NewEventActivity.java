package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewEventActivity extends AppCompatActivity {

    private EditText eventTitleView;
    private EditText eventDescriptionView;
    private TextView eventDateView;

    private ImageView dateButton;
    private EditText eventLocationView;
    private Button postEventButton;

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
        postEventButton.setOnClickListener(v -> {
            String eventTitle = eventTitleView.getText().toString();
            String eventDescription = eventDescriptionView.getText().toString();
            String eventDate = eventDateView.getText().toString();
            String eventLocation = eventLocationView.getText().toString();

            if (eventTitle.isEmpty() ||
                    eventDescription.isEmpty() ||
                    eventDate.isEmpty() ||
                    eventLocation.isEmpty()) {

                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            }
            else {
                EventPost eventPost = new EventPost("", eventTitle, eventDescription, eventDate, eventLocation);

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference eventsRef = database.getReference("events");

                String eventId = eventsRef.child(eventDate).push().getKey();
                eventPost.setEventId(eventId);

                eventsRef.child(eventDate).child(eventId).setValue(eventPost).addOnFailureListener(e -> {
                    eventsRef.child(eventId).removeValue();
                    Toast.makeText(this, "Erreur lors de l'ajout de l'évènement", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Evènement ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                });
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
                (view, year1, monthOfYear, dayOfMonth) -> eventDateView.setText(year1 + "/" + (monthOfYear + 1) + "/" + dayOfMonth), year, month, day);
        picker.show();
    }
}
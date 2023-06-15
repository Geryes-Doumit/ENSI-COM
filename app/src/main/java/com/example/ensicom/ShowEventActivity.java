package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowEventActivity extends AppCompatActivity {

    private RecyclerView eventsRV;
    ArrayList<EventPost> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String eventDate = getIntent().getStringExtra("eventDate");

        eventsRV = findViewById(R.id.EventsRV);

        eventsRV.setLayoutManager(new LinearLayoutManager(this));
        eventsRV.setAdapter(new EventRecyclerAdapter(eventsList));

        DatabaseReference showref = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("events").child(eventDate);

        showref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    EventPost event = ds.getValue(EventPost.class);
                    toolbar.setTitle(event.getEventDate());
                    eventsList.add(event);
                    eventsRV.getAdapter().notifyItemInserted(eventsList.size() - 1);
                }
            }
        });
    }
}
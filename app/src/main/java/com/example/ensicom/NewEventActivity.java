//package com.example.ensicom;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Objects;
//
//public class NewEventActivity extends AppCompatActivity{
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_event);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("New Event");
//
//     Button button = findViewById(R.id.button);
//     button.setOnClickListener(new View.OnClickListener() {
//         @Override
//         public void onClick(View v) {
//             EditText editText = findViewById(R.id.editText);
//             String content = editText.getText().toString();
//             if (content.isEmpty()) {
//                 Toast.makeText(NewEventActivity.this, "Please enter a content", Toast.LENGTH_SHORT).show();
//                 return;
//             }
//             String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//             DatabaseReference postRef = FirebaseDatabase
//                     .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
//                     .getReference("event")
//                     .push();
//             String postId = postRef.getKey();
//             String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
//             ClassicEvent event = new ClassicEvent(postId, content, date, userId);
//             postRef.setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
//                     @Override
//                     public void onSuccess(Void unused) {
//                         Toast.makeText(NewEventActivity.this, "Event created", Toast.LENGTH_SHORT).show();
//                         finish();
//                     }
//                 }).addOnFailureListener(new OnFailureListener() {
//                     @Override
//                     public void onFailure(@NonNull Exception e) {
//                         Toast.makeText(NewEventActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                     }
//                 });
//             }
//         });
//    }
//
//     @Override
//     public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//         if (item.getItemId() == android.R.id.home) {
//             finish();
//         }
//         return super.onOptionsItemSelected(item);
//     }
//
//     private void deletePost(String postId) {
//         DatabaseReference postRef = FirebaseDatabase
//                 .getInstance(DATABASE_URL)
//                 .getReference("event")
//                 .child(postId);
//         postRef.removeValue();
//     }
//
//     @Override
//     public void onBackPressed() {
//         super.onBackPressed();
//         finish();
//     }
//
//     @Override
//     public boolean onCreateOptionsMenu(Menu menu) {
//         getMenuInflater().inflate(R.menu.new_post_menu, menu);
//         return super.onCreateOptionsMenu(menu);
//     }
//}

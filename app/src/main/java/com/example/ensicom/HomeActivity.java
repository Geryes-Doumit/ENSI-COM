package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RecyclerView postsListView;
    TextView userName;
    Button settings;
    FloatingActionButton newPostButton;
    List<ClassicPost> postsList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UserAndPostRecyclerAdapter adapter = new UserAndPostRecyclerAdapter(postsList);

        // Adding the latest posts to the posts list
        DatabaseReference postsRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("posts");

        postsRef.limitToLast(20).get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                postsList.add(post);
            }
            Collections.reverse(postsList);

            // Showing the posts using the recycler view
            postsListView = findViewById(R.id.postsListView);
            postsListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            postsListView.setAdapter(adapter);
        });

        settings=findViewById(R.id.buttonHomeSettings);
        settings.setOnClickListener(v -> {
            Intent mainIntent = new Intent(HomeActivity.this, SettingsActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });

        newPostButton=findViewById(R.id.newPost);

        newPostButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(HomeActivity.this, NewPostActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }

    /**
     * @deprecated Use {@link #onSupportNavigateUp()} instead.
     * @return
     */
    @Override
    @Deprecated
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * @deprecated Use {@link #onBackPressed()} instead.
     */
    @Override
    @Deprecated
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
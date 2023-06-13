package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModerationActivity extends AppCompatActivity {
    RecyclerView postsListView;
    private List<ClassicPost> postsList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderation);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Modération");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getPosts();
        swipeRefreshLayout = findViewById(R.id.homeRefreshLayoutMod);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosts();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    public void getPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("moderationPost");

        postsRef.get().addOnCompleteListener(task -> {
            postsListView =findViewById(R.id.postsListViewMod);
            if (task.isSuccessful()) {
                postsList.clear();
                for (DataSnapshot postDateSnapshot : task.getResult().getChildren()) {
                    for (DataSnapshot postSnapshot : postDateSnapshot.getChildren()) {
                        ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                        postsList.add(post);
                    }
                }
                Collections.reverse(postsList);

                // Showing the posts using the recycler view
                postsListView.setLayoutManager(new LinearLayoutManager(ModerationActivity.this));
                postsListView.setAdapter(new ModeratorAdapter(postsList));

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}
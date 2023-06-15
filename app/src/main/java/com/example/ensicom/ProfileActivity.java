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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ImageView profilePicture;
    TextView profileName;
    String profilePictureUrl;
    RecyclerView postsListView;
    private List<ClassicPost> postsList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private String lastLoadedPostDate;
    String userUid;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lastLoadedPostDate = null;
        userUid = getIntent().getStringExtra("userId");
        profileName=findViewById(R.id.textViewProfileName);
        profilePicture=findViewById(R.id.imageViewProfilePic);
        postsListView = findViewById(R.id.profileRecyclerView);
        postsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() == postsList.size() - 1
                        && lastLoadedPostDate != null) {
                    getPosts();
                }
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("user").child(userUid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("username").getValue(String.class);
                    profileName.setText(name);
                    profilePictureUrl = dataSnapshot.child("profilePicture").getValue(String.class);
                    Glide.with(ProfileActivity.this)
                            .load(profilePictureUrl)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(profilePicture);
                    if (profilePictureUrl == null) {
                        Toast.makeText(ProfileActivity.this, "Impossible de charger l'image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "L'image n'a pas pu être récupérée", Toast.LENGTH_SHORT).show();
            }
        });
        getPosts();
        swipeRefreshLayout = findViewById(R.id.profileRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            lastLoadedPostDate = null;
            getPosts();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("posts");

        if (lastLoadedPostDate == null) {
            postsRef.orderByKey().limitToFirst(10).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    postsList.clear();
                    for (DataSnapshot postDateSnapshot : task.getResult().getChildren()) {
                        for (DataSnapshot postSnapshot : postDateSnapshot.getChildren()) {
                            if (userUid.equals(postSnapshot.child("userId").getValue(String.class))) {
                                ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                                postsList.add(post);
                                lastLoadedPostDate = post.getInvertedDate().toString();
                            }
                        }
                    }

                    // Showing the posts using the recycler view
                    postsListView.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                    postsListView.setAdapter(new UserAndPostRecyclerAdapter(postsList));
                }
            });
        }
        else {
            postsRef.orderByKey().startAfter(lastLoadedPostDate).limitToFirst(5).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot postDateSnapshot : task.getResult().getChildren()) {
                        for (DataSnapshot postSnapshot : postDateSnapshot.getChildren()) {
                            if (userUid.equals(postSnapshot.child("userId").getValue(String.class))) {
                                ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                                if (!postsList.contains(post)) {
                                    postsList.add(post);
                                    lastLoadedPostDate = post.getInvertedDate().toString();
                                    postsListView.getAdapter()
                                            .notifyItemInserted(postsList.size() - 1);
                            }
                            }
                        }
                    }
                }
            });
        }
    }
}
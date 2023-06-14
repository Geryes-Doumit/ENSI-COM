package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersActivity extends AppCompatActivity {
    RecyclerView userListView;
    List<User> userList = new ArrayList<>();
    List<String> userUidList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Utilisateurs");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getUsers();
        swipeRefreshLayout = findViewById(R.id.homeRefreshLayoutMod);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getUsers();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    public void getUsers() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("user");
        postsRef.get().addOnCompleteListener(task -> {
            userListView =findViewById(R.id.userListView);
            if (task.isSuccessful()) {
                userList.clear();
                for (DataSnapshot userSnapshot : task.getResult().getChildren()) {
                        User user = userSnapshot.getValue(User.class);
                        String uid = userSnapshot.getKey();
                        userList.add(user);
                        userUidList.add(uid);
                }
                // Showing the posts using the recycler view
                userListView.setLayoutManager(new LinearLayoutManager(UsersActivity.this));
                userListView.setAdapter(new UserAdapter(userList, userUidList));


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
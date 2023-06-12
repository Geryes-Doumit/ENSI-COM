package com.example.ensicom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView postsListView;
    private FloatingActionButton newPostButton;
    private List<ClassicPost> postsList = new ArrayList<>();
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        getPosts();
        newPostButton = view.findViewById(R.id.newPost);
        newPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            startActivity(intent);
        });



        swipeRefreshLayout = view.findViewById(R.id.homeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getPosts();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    public void getPosts() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("posts");

        postsRef.limitToLast(20).get().addOnCompleteListener(task -> {
            postsListView = view.findViewById(R.id.postsListView);
            if (isAdded() && (task.isSuccessful())) {
                postsList.clear();
                for (DataSnapshot postSnapshot : task.getResult().getChildren()) {
                    ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                    postsList.add(post);
                }
                Collections.reverse(postsList);

                // Showing the posts using the recycler view
                postsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                postsListView.setAdapter(new UserAndPostRecyclerAdapter(postsList));

            }
        });
    }

    public RecyclerView getPostsListView() {
        return postsListView;
    }

    public List<ClassicPost> getPostsList() {
        return postsList;
    }
}

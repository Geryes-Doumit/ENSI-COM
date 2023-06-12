package com.example.ensicom;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private Button settings;
    private FloatingActionButton newPostButton;
    private Button testButton;
    private List<ClassicPost> postsList = new ArrayList<>();
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        testButton = view.findViewById(R.id.buttonTest);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), ProfileActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
            }
        });

        // Adding the latest posts to the posts list
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("posts");

        postsRef.limitToLast(20).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
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
            }
        });

        newPostButton = view.findViewById(R.id.newPost);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    public RecyclerView getPostsListView() {
        return postsListView;
    }

    public List<ClassicPost> getPostsList() {
        return postsList;
    }
}

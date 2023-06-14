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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView postsListView;
    private FloatingActionButton newPostButton;
    private List<ClassicPost> postsList = new ArrayList<>();
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String lastLoadedPostDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, container, false);

        lastLoadedPostDate = null;

        postsListView = view.findViewById(R.id.postsListView);

        getPosts(false);
        newPostButton = view.findViewById(R.id.newPost);
        newPostButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewPostActivity.class);
            startActivity(intent);
        });

        postsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager.findLastVisibleItemPosition() == postsList.size() - 1
                        && lastLoadedPostDate != null) {
                    boolean showToast = false;

                    if (layoutManager.findLastCompletelyVisibleItemPosition() == postsList.size() - 1) {
                        showToast = true;
                    }
                    getPosts(showToast);
                }
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.homeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            lastLoadedPostDate = null;
            getPosts(false);
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    public void getPosts(boolean toast) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("posts");

        if (lastLoadedPostDate == null) {
            postsRef.orderByKey().limitToFirst(10).get().addOnCompleteListener(task -> {
                if (isAdded() && (task.isSuccessful())) {
                    postsList.clear();
                    for (DataSnapshot postDateSnapshot : task.getResult().getChildren()) {
                        for (DataSnapshot postSnapshot : postDateSnapshot.getChildren()) {
                            ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                            postsList.add(post);
                            lastLoadedPostDate = post.getInvertedDate().toString();
                        }
                    }

                    // Showing the posts using the recycler view
                    postsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    postsListView.setAdapter(new UserAndPostRecyclerAdapter(postsList));
                }
                else {
                    Toast.makeText(getActivity(), "Aucun post trouvé.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            postsRef.orderByKey().startAfter(lastLoadedPostDate).limitToFirst(5).get().addOnCompleteListener(task -> {
                List<ClassicPost> testForSizeList = new ArrayList<>();
                if (isAdded() && (task.isSuccessful())) {
                    for (DataSnapshot postDateSnapshot : task.getResult().getChildren()) {
                        for (DataSnapshot postSnapshot : postDateSnapshot.getChildren()) {
                            ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                            if (!postsList.contains(post)) {
                                postsList.add(post);
                                testForSizeList.add(post);
                                lastLoadedPostDate = post.getInvertedDate().toString();
                            }
                        }
                        postsListView.getAdapter().notifyDataSetChanged();
                    }
                }
                if (testForSizeList.size() == 0 && toast) {
                    Toast message = Toast.makeText(view.getContext(), "Tous les posts on été chargés.", Toast.LENGTH_SHORT);
                    message.show();
                }
            });
        }
    }

    public RecyclerView getPostsListView() {
        return postsListView;
    }

    public List<ClassicPost> getPostsList() {
        return postsList;
    }
}

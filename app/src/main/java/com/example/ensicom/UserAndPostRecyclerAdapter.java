package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAndPostRecyclerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    List<ClassicPost> postsList;

    public UserAndPostRecyclerAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_and_post_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ClassicPost post = postsList.get(position);

        String postContent = post.getContent();
        String postPicture1= post.getPictureUrl1();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
                .child(post.getUserId());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User postUser = dataSnapshot.getValue(User.class);
                String postUserName = postUser.getUsername();
                String profilePictureUrl = postUser.getProfilePicture();

                holder.getUserName().setText(postUserName);
                holder.getPostContent().setText(postContent);
                Glide.with(holder.getUserProfilePicture().getContext()).load(profilePictureUrl).into(holder.getUserProfilePicture());
                Glide.with(holder.getPostPicture1().getContext()).load(postPicture1).into(holder.getPostPicture1());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

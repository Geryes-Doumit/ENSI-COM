package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    private List<Comment> comments;

    public CommentsRecyclerAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        String commentContent = comment.getContent();
        holder.getShowRepliesButton().setBackground(null);
        holder.getShowMoreArrow().setBackground(null);

        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
                .child(comment.getUserId());

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            User commentUser = dataSnapshot.getValue(User.class);
            String commentUserName = commentUser.getUsername();

            holder.getCommentUserName().setText(commentUserName);
            holder.getCommentContent().setText(commentContent);
            Glide.with(holder.getCommentProfilePicture().getContext()).load(commentUser.getProfilePicture()).circleCrop().into(holder.getCommentProfilePicture());
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

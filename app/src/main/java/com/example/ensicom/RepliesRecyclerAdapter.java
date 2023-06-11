package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RepliesRecyclerAdapter extends RecyclerView.Adapter<RepliesViewHolder> {

    private ArrayList<CommentReply> repliesList;

    public RepliesRecyclerAdapter(ArrayList<CommentReply> repliesList) {
        this.repliesList = repliesList;
    }

    @NonNull
    @Override
    public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.reply_item, parent, false);
        return new RepliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepliesViewHolder holder, int position) {
        CommentReply reply = repliesList.get(position);
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference userRef = database
                .getReference("user").child(reply.getUserId());

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            User replyUser = dataSnapshot.getValue(User.class);
            String replyUserName = replyUser.getUsername();
            String replyContent = reply.getContent();

            holder.getReplyUserName().setText(replyUserName);
            holder.getReplyContent().setText(replyContent);
        });
    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }
}

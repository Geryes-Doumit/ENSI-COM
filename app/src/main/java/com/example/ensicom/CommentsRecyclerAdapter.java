package com.example.ensicom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsViewHolder> {
    private List<Comment> comments;
    private User commentUser;
    private Comment replyComment;
    private Boolean replyMode = false;
    private ArrayList<CommentReply> replies = new ArrayList<>();

    public CommentsRecyclerAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentsViewHolder(view, (EditText) parent.getRootView().findViewById(R.id.newCommentContent));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        String commentContent = comment.getContent();

        Button replyButton = holder.getReplyButton();
        Button showRepliesButton = holder.getShowRepliesButton();
        ImageView showMoreArrow = holder.getShowMoreArrow();
        holder.getRepliesRecyclerView().setVisibility(View.GONE);

        showRepliesButton.setBackground(null);
        showMoreArrow.setBackground(null);
        replyButton.setBackground(null);

        if (comment.getRepliesId().equals("")) {
            showRepliesButton.setVisibility(View.GONE);
            showMoreArrow.setVisibility(View.GONE);
        } else {
            showRepliesButton.setVisibility(View.VISIBLE);
            showMoreArrow.setVisibility(View.VISIBLE);
        }

        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
                .child(comment.getUserId());

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            commentUser = dataSnapshot.getValue(User.class);
            String commentUserName = commentUser.getUsername();

            holder.getCommentUserName().setText(commentUserName);
            holder.getCommentContent().setText(commentContent);
            Glide.with(holder.getCommentProfilePicture().getContext()).load(commentUser.getProfilePicture()).circleCrop().into(holder.getCommentProfilePicture());
        });

        replyButton.setOnClickListener(v -> {
            this.setReplyComment(comment);
            this.setReplyMode(true);
            holder.getNewCommentContent().requestFocus();
            holder.getNewCommentContent().setHint("Répondre à " + commentUser.getUsername());
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(v.getContext().INPUT_METHOD_SERVICE);
            imm.showSoftInput(holder.getNewCommentContent(), InputMethodManager.SHOW_IMPLICIT);
        });

        showRepliesButton.setOnClickListener(v -> showOrHideReplies(holder, comment));
        showMoreArrow.setOnClickListener(v -> showOrHideReplies(holder, comment));
    }

    public void showOrHideReplies(CommentsViewHolder holder, Comment comment) {
        RecyclerView repliesRecyclerView = holder.getRepliesRecyclerView();
        if (!repliesRecyclerView.isShown()) {
            FirebaseDatabase database = FirebaseDatabase
                    .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference repliesRef = database
                    .getReference("replyLists")
                    .child(comment.getRepliesId())
                    .child("replies");
            repliesRef.get().addOnSuccessListener(dataSnapshot -> {
                if (dataSnapshot.exists()) {
                    replies.clear();
                    for (DataSnapshot replySnapshot : dataSnapshot.getChildren()) {
                        CommentReply reply = replySnapshot.getValue(CommentReply.class);
                        replies.add(reply);
                    }
                    replies.remove(0);
                    repliesRecyclerView.setAdapter(new RepliesRecyclerAdapter(replies));
                    repliesRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                    repliesRecyclerView.setVisibility(View.VISIBLE);
                    holder.getShowRepliesButton().setText("Masquer");
                    holder.getShowMoreArrow().setRotation(180);
                }
                else {
                    comment.setRepliesId("");
                    holder.getShowRepliesButton().setVisibility(View.GONE);
                    holder.getShowMoreArrow().setVisibility(View.GONE);
                    DatabaseReference commentRef = database.getReference()
                            .child("commentLists")
                            .child(comment.getCommentsListId())
                            .child("comments")
                            .child(comment.getCommentPosition().toString());
                    commentRef.setValue(comment);
                }
            });
        } else {
            repliesRecyclerView.setVisibility(View.GONE);
            holder.getShowRepliesButton().setText("Réponses");
            holder.getShowMoreArrow().setRotation(0);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public void setReplyComment(Comment replyComment) {
        this.replyComment = replyComment;
    }
    public Comment getReplyComment() {
        return replyComment;
    }
    public void setReplyMode(Boolean replyMode) {
        this.replyMode = replyMode;
    }
    public Boolean getReplyMode() {
        return replyMode;
    }
}

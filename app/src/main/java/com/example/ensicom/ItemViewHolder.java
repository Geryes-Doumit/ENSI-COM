package com.example.ensicom;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder{
    private TextView userName;
    private ImageView userProfilePicture;
    private TextView postContent;
//    private TextView postDate;
    private TextView likeCount;
    private TextView commentCount;
    private Button likeButton;
    private Button commentButton;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName);
        userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
        postContent = itemView.findViewById(R.id.postContent);
//        postDate = itemView.findViewById(R.id.postDate);
        likeCount = itemView.findViewById(R.id.likeCount);
        commentCount = itemView.findViewById(R.id.commentCount);
        likeButton = itemView.findViewById(R.id.likeButton);
        commentButton = itemView.findViewById(R.id.commentButton);
    }

    // All the getters
    public TextView getUserName() {
        return userName;
    }

    public ImageView getUserProfilePicture() {
        return userProfilePicture;
    }

    public TextView getPostContent() {
        return postContent;
    }

//    public TextView getPostDate() {
//        return postDate;
//    }

    public TextView getLikeCount() {
        return likeCount;
    }

    public TextView getCommentCount() {
        return commentCount;
    }

    public Button getLikeButton() {
        return likeButton;
    }

    public Button getCommentButton() {
        return commentButton;
    }
}

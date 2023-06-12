package com.example.ensicom;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAndPostViewHolder extends RecyclerView.ViewHolder{
    private TextView userName;
    private ImageView userProfilePicture;
    private ImageView postPicture1;
    private TextView postContent;
    private TextView likeCount;
    private TextView commentCount;
    private Button likeButton;
    private Button commentButton;
    private ImageButton deletePostButton;
    private TextView tagList;
    private VideoView videoView;

    public UserAndPostViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName);
        userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
        postContent = itemView.findViewById(R.id.postContent);
        likeCount = itemView.findViewById(R.id.likeCount);
        commentCount = itemView.findViewById(R.id.commentCount);
        likeButton = itemView.findViewById(R.id.likeButton);
        commentButton = itemView.findViewById(R.id.commentButton);
        postPicture1 = itemView.findViewById(R.id.imageContent1);
        deletePostButton = itemView.findViewById(R.id.deletePostButton);
        tagList = itemView.findViewById(R.id.postTags);
        videoView= itemView.findViewById(R.id.videoView);
    }

    // All the getters
    public VideoView getVideoView() {
        return videoView;
    }
    public TextView getTagList() {
        return tagList;
    }
    public ImageView getPostPicture1() {
        return postPicture1;
    }
    public TextView getUserName() {
        return userName;
    }

    public ImageView getUserProfilePicture() {
        return userProfilePicture;
    }

    public TextView getPostContent() {
        return postContent;
    }
    
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

    public ImageButton getDeletePostButton() {
        return deletePostButton;
    }
}
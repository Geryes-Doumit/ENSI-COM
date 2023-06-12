package com.example.ensicom;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModeratorHolder extends RecyclerView.ViewHolder {
    private TextView userName;
    private ImageView userProfilePicture;
    private ImageView postPicture1;
    private TextView postContent;
    private Button validatePostButton;
    private Button refusePostButton;
    private TextView tagList;
    private ImageView videoPlayer;

    public ModeratorHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.userName);
        userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
        postContent = itemView.findViewById(R.id.postContent);
        postPicture1 = itemView.findViewById(R.id.imageContent1);
        tagList = itemView.findViewById(R.id.postTags);
        validatePostButton = itemView.findViewById(R.id.adminValidPost);
        refusePostButton = itemView.findViewById(R.id.adminDeletePost);
        videoPlayer = itemView.findViewById(R.id.moderationVideoPlayer);
    }
    public ImageView getVideoPlayer() {
        return videoPlayer;
    }
    public Button getValidatePostButton() {
        return validatePostButton;
    }
    public Button getRefusePostButton() {
        return refusePostButton;
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

}

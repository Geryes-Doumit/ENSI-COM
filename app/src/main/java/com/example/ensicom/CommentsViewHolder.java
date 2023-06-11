package com.example.ensicom;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        private TextView commentUserName;
        private TextView commentContent;
        private ImageView commentProfilePicture;
        private Button showRepliesButton;
        private ImageView showMoreArrow;
        private Button replyButton;
        private EditText newCommentContent;
        private RecyclerView repliesRecyclerView;

        public CommentsViewHolder(@NonNull View itemView, EditText newCommentContent) {
            super(itemView);

            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentContent = itemView.findViewById(R.id.commentContent);
            commentProfilePicture = itemView.findViewById(R.id.commentProfilePicture);
            showRepliesButton = itemView.findViewById(R.id.showRepliesButton);
            showMoreArrow = itemView.findViewById(R.id.showMoreArrow);
            replyButton = itemView.findViewById(R.id.replyButton);
            repliesRecyclerView = itemView.findViewById(R.id.repliesRecyclerView);
            this.newCommentContent = newCommentContent;
        }

        // All the getters
        public TextView getCommentUserName() {
            return commentUserName;
        }
        public TextView getCommentContent() {
            return commentContent;
        }
        public ImageView getCommentProfilePicture() {
            return commentProfilePicture;
        }
        public Button getShowRepliesButton() {
            return showRepliesButton;
        }
        public ImageView getShowMoreArrow() {
            return showMoreArrow;
        }
        public Button getReplyButton() {
            return replyButton;
        }
        public EditText getNewCommentContent() {
            return newCommentContent;
        }
        public RecyclerView getRepliesRecyclerView() {
            return repliesRecyclerView;
        }
}

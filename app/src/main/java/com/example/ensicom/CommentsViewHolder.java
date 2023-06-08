package com.example.ensicom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

    public class CommentsViewHolder extends RecyclerView.ViewHolder{
        private TextView commentUserName;
        private TextView commentContent;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            commentUserName = itemView.findViewById(R.id.commentUserName);
            commentContent = itemView.findViewById(R.id.commentContent);
        }

        // All the getters
        public TextView getCommentUserName() {
            return commentUserName;
        }

        public TextView getCommentContent() {
            return commentContent;
        }
}

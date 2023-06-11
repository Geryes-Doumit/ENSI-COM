package com.example.ensicom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RepliesViewHolder extends RecyclerView.ViewHolder{
    private TextView replyUserName;
    private TextView replyContent;


    public RepliesViewHolder(@NonNull View itemView) {
        super(itemView);
        replyUserName = itemView.findViewById(R.id.replyUserName);
        replyContent = itemView.findViewById(R.id.replyContent);
    }

    // All the getters
    public TextView getReplyUserName() {
        return replyUserName;
    }
    public TextView getReplyContent() {
        return replyContent;
    }
}

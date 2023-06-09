package com.example.ensicom;

import java.util.ArrayList;

public class CommentRepliesList {
    private String commentID;
    private ArrayList<CommentReply> replies = new ArrayList<>();

    public CommentRepliesList() {
    }

    public CommentRepliesList(String commentID) {
        this.commentID = commentID;
//        this.replies.add(new CommentReply("","", "", ""));
    }
}

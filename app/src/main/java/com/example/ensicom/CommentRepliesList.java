package com.example.ensicom;

import java.util.ArrayList;

public class CommentRepliesList {
    private ArrayList<CommentReply> replies = new ArrayList<>();
    private String repliesListId;

    public CommentRepliesList() {
    }

    public CommentRepliesList(String repliesListId) {
        this.repliesListId = repliesListId;
        replies = new ArrayList<>();
        replies.add(new CommentReply("", "", 0));
    }

    public String getRepliesListId() {
        return repliesListId;
    }
    public void setRepliesListId(String repliesListId) {
        this.repliesListId = repliesListId;
    }
    public void addReply(CommentReply reply) {
        this.replies.add(reply);
    }
    public void removeReply(CommentReply reply) {
        this.replies.remove(reply);
    }
    public ArrayList<CommentReply> getReplies() {
        return replies;
    }
}

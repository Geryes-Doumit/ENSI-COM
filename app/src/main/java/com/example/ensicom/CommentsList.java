package com.example.ensicom;

import java.util.ArrayList;
import java.util.List;

public class CommentsList {
    private List<Comment> comments = new ArrayList<>();
    private String commentsListId;

    public CommentsList() {
    }

    public CommentsList(String commentsListId) {
        this.commentsListId = commentsListId;
        comments = new ArrayList<>();
        comments.add(new Comment("", 0, "", 0L));
    }

    public String getCommentsListId() {
        return commentsListId;
    }

    public void setCommentsListId(String commentsListId) {
        this.commentsListId = commentsListId;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }
}

package com.example.ensicom;

public class Comment {
    private String content;
    private String userId;
    private Integer commentId;

    public Comment() {
    }

    public Comment(String content, String userId, Integer commentId) {
        this.content = content;
        this.userId = userId;
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }
}

package com.example.ensicom;

public class Comment {
    String userId;
    Integer commentId;
    String content;
    Long date;

    public Comment() {
    }

    public Comment(String userId, Integer commentId, String content, Long date) {
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getContent() {
        return content;
    }

    public Long getDate() {
        return date;
    }
}

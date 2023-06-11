package com.example.ensicom;

public class Comment {
    String userId;
    Integer commentPosition;
    String repliesId;
    String content;
    Long date;
    private String commentsListId;

    public Comment() {
    }

    public Comment(String userId, Integer commentPosition, String content, Long date) {
        this.userId = userId;
        this.commentPosition = commentPosition;
        this.content = content;
        this.date = date;
        this.repliesId = "";
        this.commentsListId = "";
    }

    public String getUserId() {
        return userId;
    }
    public Integer getCommentPosition() {
        return commentPosition;
    }
    public String getContent() {
        return content;
    }
    public Long getDate() {
        return date;
    }
    public String getRepliesId() {
        return repliesId;
    }
    public void setRepliesId(String repliesId) {
        this.repliesId = repliesId;
    }
    public void setCommentsListId(String commentsListId) {
        this.commentsListId = commentsListId;
    }
    public String getCommentsListId() {
        return commentsListId;
    }
}

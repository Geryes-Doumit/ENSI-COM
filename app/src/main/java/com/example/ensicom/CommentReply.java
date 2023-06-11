package com.example.ensicom;

public class CommentReply {
    private String userId;
    private String content;
    private Integer replyPosition;

    public CommentReply() {
    }

    public CommentReply(String content, String userId, Integer replyPosition) {
        this.userId = userId;
        this.content = content;
        this.replyPosition = replyPosition;
    }

    public String getContent() {
        return content;
    }
    public Integer getReplyPosition() {
        return replyPosition;
    }
    public String getUserId() {
        return userId;
    }
}

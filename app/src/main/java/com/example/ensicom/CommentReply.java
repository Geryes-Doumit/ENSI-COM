package com.example.ensicom;

public class CommentReply {
    private String userId;
    private String content;
    private String commentId;
    private Integer replyId;

    public CommentReply() {
    }

    public CommentReply(String content, String userId, String commentId) {
        this.userId = userId;
        this.content = content;
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }
    public String getCommentId() {
        return commentId;
    }
    public Integer getReplyId() {
        return replyId;
    }
    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }
    public String getUserId() {
        return userId;
    }
}

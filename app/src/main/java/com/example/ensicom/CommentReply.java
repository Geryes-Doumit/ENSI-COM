package com.example.ensicom;

public class CommentReply {
    private String content;
    private String postId;
    private String commentId;

    public CommentReply() {
    }

    public CommentReply(String content, String postId, String commentId) {
        this.content = content;
        this.postId = postId;
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public String getPostId() {
        return postId;
    }

    public String getCommentId() {
        return commentId;
    }
}

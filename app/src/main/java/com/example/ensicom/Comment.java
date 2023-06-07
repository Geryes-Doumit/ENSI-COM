package com.example.ensicom;

public class Comment {
    private String content;
    private String userId;
    private String postId;

    public Comment() {
    }

    public Comment(String content, String userId, String postId) {
        this.content = content;
        this.userId = userId;
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public String getPostId() {
        return postId;
    }
}

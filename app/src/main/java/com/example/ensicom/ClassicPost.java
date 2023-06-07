package com.example.ensicom;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class ClassicPost {
    private String content;
    private String postId;
    private Long date;
    private String userId;
    private Integer commentCount;
    private ArrayList<String> likes;

    public ClassicPost() {
    }

    public ClassicPost(String content, String userId, Long date) {
        this.content = content;
        this.userId = userId;
        this.date = date;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getLikeUserList() {
        ArrayList<String> ar = new ArrayList<String>();
        ar.add("toto");
        ar.add("tata");
        return ar;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Long getDate() {
        return date;
    }

}

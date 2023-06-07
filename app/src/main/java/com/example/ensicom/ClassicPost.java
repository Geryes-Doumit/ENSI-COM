package com.example.ensicom;

import androidx.annotation.NonNull;

import java.util.Date;

public class ClassicPost {
    private String content;
    private Long date;
    private String userId;
    private Integer likeCount;
    private Integer commentCount;
    private String pictureUrl;

    public ClassicPost() {
    }



    public ClassicPost(String content, String userId, Long date, String pictureUrl) {
        this.content = content;
        this.userId = userId;
        this.date = date;
        this.pictureUrl = pictureUrl;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public Integer getLikeCount() {
        return likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Long getDate() {
        return date;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}

package com.example.ensicom;

import androidx.annotation.NonNull;

import java.util.Date;

public class ClassicPost {
    private String content;
    private Long date;
    private String userId;
    private Integer likeCount;
    private Integer commentCount;
    private String pictureUrl1;
    private String pictureUrl2;
    private String pictureUrl3;
    private String pictureUrl4;

    public String getPictureUrl2() {
        return pictureUrl2;
    }

    public void setPictureUrl2(String getPictureUrl2) {
        this.pictureUrl2 = getPictureUrl2;
    }

    public String getPictureUrl3() {
        return pictureUrl3;
    }

    public void setPictureUrl3(String getPictureUrl3) {
        this.pictureUrl3 = getPictureUrl3;
    }

    public String getPictureUrl4() {
        return pictureUrl4;
    }

    public void setPictureUrl4(String getPictureUrl4) {
        this.pictureUrl4 = getPictureUrl4;
    }

    public String getPictureUrl1() {
        return pictureUrl1;
    }

    public void setPictureUrl1(String pictureUrl1) {
        this.pictureUrl1 = pictureUrl1;
    }

    private String pictureUrl1;

    public ClassicPost() {
    }

    public ClassicPost(String content, String userId, Long date, String pictureUrl1) {
        this.content = content;
        this.userId = userId;
        this.date = date;
        this.pictureUrl1 = pictureUrl1;
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

    public String getPictureUrl1() {
        return pictureUrl1;
    }

    public void setPictureUrl1(String pictureUrl) {
        this.pictureUrl1 = pictureUrl;
    }
}

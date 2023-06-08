package com.example.ensicom;

import java.util.ArrayList;

public class ClassicPost {
    private String content;
    private String postId;
    private Long date;
    private String userId;
    private Integer likeCount;
    private Integer commentCount;
    private ArrayList<String> likeUserList;
    private ArrayList<Comment> commentsList;

    private String pictureUrl1;
    private String pictureUrl2;
    private String pictureUrl3;
    private String pictureUrl4;

    public ClassicPost() {
    }

    public ClassicPost(String postId, String content, String userId, Long date, String pictureUrl1, String pictureUrl2, String pictureUrl3, String pictureUrl4) {
        this.postId = postId;
        this.content = content;
        this.userId = userId;
        this.date = date;
        this.pictureUrl1 = pictureUrl1;
        this.pictureUrl2 = pictureUrl2;
        this.pictureUrl3 = pictureUrl3;
        this.pictureUrl4 = pictureUrl4;
        this.commentsList = new ArrayList<>();
        this.commentsList.add(new Comment("", "", 0));
        this.commentCount = 0;
        this.likeUserList = new ArrayList<>();
        this.likeUserList.add("");
        this.likeCount = 0;
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
        return likeUserList;
    }

    public void setLikeUserList(ArrayList<String> likeUserList) {
        this.likeUserList = likeUserList;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void addLike(String userId) {
        setLikeCount(getLikeCount() + 1);
        this.likeUserList.add(userId);
    }

    public void removeLike(String userId) {
        setLikeCount(getLikeCount() - 1);
        this.likeUserList.remove(userId);
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

    public void setPictureUrl1(String pictureUrl1) {
        this.pictureUrl1 = pictureUrl1;
    }

    public String getPictureUrl2() {
        return pictureUrl2;
    }

    public void setPictureUrl2(String pictureUrl2) {
        this.pictureUrl2 = pictureUrl2;
    }

    public String getPictureUrl3() {
        return pictureUrl3;
    }

    public void setPictureUrl3(String pictureUrl3) {
        this.pictureUrl3 = pictureUrl3;
    }

    public String getPictureUrl4() {
        return pictureUrl4;
    }

    public void setPictureUrl4(String pictureUrl4) {
        this.pictureUrl4 = pictureUrl4;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return this.postId;
    }

    public ArrayList<Comment> getCommentsList() {
        return commentsList;
    }

    public void addComment(Comment comment) {
        this.commentsList.add(comment);
        this.commentCount++;
    }

    public void removeComment(Comment comment) {
        this.commentsList.remove(comment);
        this.commentCount--;
    }
}


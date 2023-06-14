package com.example.ensicom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClassicPost {
    private String content;
    private String postId;
    private Long date;
    private Long invertedDate;
    private String userId;
    private String commentsId; // To get to the comments
    private Integer likeCount;
    private Integer commentCount;
    private ArrayList<String> likeUserList;
    private List<String> tagsList = new ArrayList<>();
    private List<String> pictureUrlList;
    private String videoUrl;

    public ClassicPost() {
    }

    public ClassicPost(String postId, String content, String userId, Long date, List<String> pictureUrlList, List<String> tagsList, String videoUrl) {
        this.postId = postId;
        this.content = content;
        this.userId = userId;
        this.date = date;
        this.invertedDate = new Date(2099, 12, 31).getTime() - this.date;
        this.pictureUrlList = pictureUrlList;
        this.commentsId = ""; // Initialize the commentId to be able to check if it's empty
        this.commentCount = 0;
        this.likeUserList = new ArrayList<>();
        this.likeUserList.add("");
        this.likeCount = 0;
        this.tagsList=tagsList;
        this.videoUrl = videoUrl;
    }

    public List<String> getPictureUrlList() {
        return pictureUrlList;
    }
    public List<String> getTagsList() {
        return tagsList;
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

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return this.postId;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCommentsId() {
        return this.commentsId;
    }

    public void setCommentsId(String commentsId) {
        this.commentsId = commentsId;
    }

    public void setDate(Long date) {
        this.date = date;
        this.invertedDate = new Date(2099, 12, 31).getTime() - this.date;
    }

    public Long getInvertedDate() {
        return this.invertedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ClassicPost)) {
            return false;
        }
        ClassicPost classicPost = (ClassicPost) o;
        return this.postId.equals(classicPost.getPostId());
    }

}


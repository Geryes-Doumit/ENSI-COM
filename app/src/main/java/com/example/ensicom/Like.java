package com.example.ensicom;

import java.util.ArrayList;

public class Like extends ClassicPost{

    String userId;
    Integer likeCount;
    ArrayList<String> likeUserList;

    public Like() {
        this.likeUserList = new ArrayList<String>();
        this.likeCount = 0;
    }

    public Integer getLikeCount() {
        return likeUserList.size();
    }

    public ArrayList<String> getLikeUserList() {
        return likeUserList;
    }

    public void addLike(String userId) {
        this.likeUserList.add(userId);
    }

    public void removeLike(String userId) {
        this.likeUserList.remove(userId);
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}

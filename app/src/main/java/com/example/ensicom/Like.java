package com.example.ensicom;

import java.util.ArrayList;

public class Like extends ClassicPost{

    String userId;
    Integer likeCount;
    ArrayList<String> likeUserList;

    public Like() {
        this.likeUserList = new ArrayList<>();
        this.likeCount = 0;
    }

    @Override
    public Integer getLikeCount() {
        return likeUserList.size();
    }

    @Override
    public ArrayList<String> getLikeUserList() {
        return likeUserList;
    }

    @Override
    public void addLike(String userId) {
        this.likeUserList.add(userId);
    }

    @Override
    public void removeLike(String userId) {
        this.likeUserList.remove(userId);
    }

    @Override
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}

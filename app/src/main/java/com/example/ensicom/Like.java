package com.example.ensicom;

import java.util.ArrayList;

public class Like {

    String userId;
    String postID;
    Integer likeCount;
    ArrayList<String> likeUserList;

    public Like(String userId, String postID) {
        this.likeUserList.add(userId);
        this.likeCount = 3;
        this.postID = postID;
    }

    public Integer getLikeCount() {
        return likeUserList.size();
    }
}

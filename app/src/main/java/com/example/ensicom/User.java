package com.example.ensicom;

public class User {
    private String username;
    private String email;
    private String profilePicture;
    private boolean admin;

    public User() {
    }
    public User(String username, String email, String profilePicture, boolean admin) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.admin = admin;
    }
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

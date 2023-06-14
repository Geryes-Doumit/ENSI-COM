package com.example.ensicom;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class UserHolder extends RecyclerView.ViewHolder {
    private TextView userName;
    private ImageView userProfilePicture;
    private Button putAdmin;
    private Button deleteAdmin;
    private TextView userStatus;
    public UserHolder(@NonNull View itemView) {
        super(itemView);
        userName = itemView.findViewById(R.id.userName);
        userProfilePicture = itemView.findViewById(R.id.userProfilePicture);
        putAdmin = itemView.findViewById(R.id.userPutAdmin);
        deleteAdmin = itemView.findViewById(R.id.userDeleteAdmin);
        userStatus = itemView.findViewById(R.id.userStatus);
    }
    public TextView getUserStatus() {
        return userStatus;
    }
    public Button getPutAdmin() {
        return putAdmin;
    }
    public Button getDeleteAdmin() {
        return deleteAdmin;
    }
    public TextView getUserName() {
        return userName;
    }
    public ImageView getUserProfilePicture() {
        return userProfilePicture;
    }
}

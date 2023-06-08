package com.example.ensicom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserAndPostRecyclerAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    List<ClassicPost> postsList;

    public UserAndPostRecyclerAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_and_post_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int currentPosition = position;
        ClassicPost post = postsList.get(position);

        String postContent = post.getContent();
        String postPicture1= post.getPictureUrl1();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("user")
                .child(post.getUserId());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User postUser = dataSnapshot.getValue(User.class);
                String postUserName = postUser.getUsername();
                String profilePictureUrl = postUser.getProfilePicture();
                String postId = post.getPostId();
                if (dataSnapshot.getKey().equals(currentUserUid)) {
                    holder.getDeletePostButton().setVisibility(View.VISIBLE);
                }
                else {
                    holder.getDeletePostButton().setVisibility(View.GONE);
                }
                holder.getDeletePostButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(v.getContext()).setTitle("Supprimer le post")
                                .setMessage("Êtes-vous sûr de vouloir supprimer ce post ?")
                                .setPositiveButton(android.R.string.yes,null)
                                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletePost(postId);
                                        Toast.makeText(v.getContext(), "Post supprimé", Toast.LENGTH_SHORT).show();
                                        postsList.remove(currentPosition);
                                        notifyDataSetChanged();
                                       }

                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setNegativeButton("Non", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });
                holder.getUserName().setText(postUserName);
                holder.getPostContent().setText(postContent);
                Glide.with(holder.getUserProfilePicture().getContext()).load(profilePictureUrl).into(holder.getUserProfilePicture());
                Glide.with(holder.getPostPicture1().getContext()).load(postPicture1).into(holder.getPostPicture1());
            }
        });
    }
    public void deletePost(String postId) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("posts")
                .child(postId);
        postRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
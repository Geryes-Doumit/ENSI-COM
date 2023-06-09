package com.example.ensicom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        String postPicture1 = post.getPictureUrl1();
        Integer likeCount = post.getLikeCount();
        String videoUrl = post.getVideoUrl();
        ArrayList<String> tags = post.getTagsList();

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
                } else {
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
                holder.getLikeCount().setText(likeCount.toString());
                holder.getCommentCount().setText(post.getCommentCount().toString());
                StringBuilder stringBuilder = new StringBuilder();
                for (String tag : tags) {
                    if (!tag.equals("")) {
                        stringBuilder.append("#").append(tag).append("; ");
                    }
                }
                String tag = stringBuilder.toString();
                holder.getTagList().setText(tag);
                Glide.with(holder.getUserProfilePicture().getContext()).load(profilePictureUrl).into(holder.getUserProfilePicture());
                Glide.with(holder.getPostPicture1().getContext()).load(postPicture1).into(holder.getPostPicture1());
                if (videoUrl != null) {
                    holder.getVideoView().setVisibility(View.VISIBLE);
                    holder.getVideoView().setVideoPath(videoUrl);
                    holder.getVideoView().start();
                } else {
                    holder.getVideoView().setVisibility(View.GONE);
                }
            }
        });
        holder.getLikeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = post.getPostId();
                DatabaseReference postRef = FirebaseDatabase
                        .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("posts")
                        .child(postId);
                postRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        ClassicPost post = dataSnapshot.getValue(ClassicPost.class);
                        if (post.getLikeUserList().toString().contains(currentUserUid)){
                            Integer likeCount = post.getLikeCount();
                            post.removeLike(currentUserUid);
                            postRef.setValue(post);
                            holder.getLikeCount().setText(likeCount.toString());
                        } else {
                            Integer likeCount = post.getLikeCount();
                            post.addLike(currentUserUid);
                            postRef.setValue(post);
                            holder.getLikeCount().setText(likeCount.toString());
                        }
                    }
                });
            }
        });

        holder.getCommentButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = post.getPostId();
                Intent intent = new Intent(v.getContext(), ShowCommentsActivity.class);
                intent.putExtra("postId", postId);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void deletePost(String postId) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("posts")
                .child(postId);

        postRef.child("commentId").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String commentId = dataSnapshot.getValue(String.class);
                DatabaseReference commentsRef = FirebaseDatabase
                        .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                        .getReference("commentLists")
                        .child(commentId);
                commentsRef.removeValue();
            }
        });
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

package com.example.ensicom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserAndPostRecyclerAdapter extends RecyclerView.Adapter<UserAndPostViewHolder> {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/";

    List<ClassicPost> postsList;
    String videoUrl;

    public UserAndPostRecyclerAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public UserAndPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_and_post_item, parent, false);
        return new UserAndPostViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull UserAndPostViewHolder holder, int position) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int currentPosition = position;
        ClassicPost post = postsList.get(position);

        String postContent = post.getContent();
        Integer likeCount = post.getLikeCount();
        videoUrl = post.getVideoUrl();
        List<String> tags = post.getTagsList();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("user")
                .child(post.getUserId());
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            User postUser = dataSnapshot.getValue(User.class);
            String postUserName = postUser.getUsername();
            String profilePictureUrl = postUser.getProfilePicture();
            Boolean isAdmin = postUser.isAdmin();
            String postId = post.getPostId();
            holder.getRefusePostButton().setVisibility(View.GONE);
            holder.getValidatePostButton().setVisibility(View.GONE);
            if (dataSnapshot.getKey().equals(currentUserUid)) {
                holder.getDeletePostButton().setVisibility(View.VISIBLE);
            } else {
                holder.getDeletePostButton().setVisibility(View.GONE);
            }

            holder.getDeletePostButton().setOnClickListener(v -> new AlertDialog.Builder(v.getContext()).setTitle("Supprimer le post")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce post ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        deletePost(postId);
                        Toast.makeText(v.getContext(), "Post supprimé", Toast.LENGTH_SHORT).show();
                        postsList.remove(currentPosition);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Non", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show());
            holder.getUserName().setText(postUserName);
            holder.getPostContent().setText(postContent);
            holder.getLikeCount().setText(likeCount.toString());
            holder.getCommentCount().setText(post.getCommentCount().toString());
            StringBuilder stringBuilder = new StringBuilder();
            for (String tag : tags) {
                if (!tag.equals("")) {
                    stringBuilder.append("#").append(tag).append(" ");
                }
            }
            String tag = stringBuilder.toString();
            holder.getTagList().setText(tag);
            Glide.with(holder.getUserProfilePicture().getContext()).load(profilePictureUrl).circleCrop().into(holder.getUserProfilePicture());
            if (post.getPictureUrlList() != null) {
                Glide.with(holder.getPostPicture1().getContext()).load(post.getPictureUrlList().get(0)).into(holder.getPostPicture1());
            }
            else {
                holder.getPostPicture1().setVisibility(View.GONE);

            }
            if (videoUrl != null) {
                holder.getVideoView().setVisibility(View.VISIBLE);
                holder.getVideoView().setVideoPath(videoUrl);
                holder.getVideoView().setOnPreparedListener(mp -> mp.setLooping(true));
                holder.getVideoView().setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (holder.getVideoView().isPlaying()) {
                            holder.getVideoView().pause();
                        } else {
                            holder.getVideoView().start();
                        }
                        return true;
                    }
                    return false;
                });

            } else {
                holder.getVideoView().setVisibility(View.GONE);
            }
        });
        holder.getLikeButton().setOnClickListener(view -> {
            String postId = post.getPostId();
            DatabaseReference postRef = FirebaseDatabase
                    .getInstance(DATABASE_URL)
                    .getReference("posts")
                    .child(postId);
            postRef.get().addOnSuccessListener(dataSnapshot -> {
                ClassicPost post1 = dataSnapshot.getValue(ClassicPost.class);
                if (post1.getLikeUserList().toString().contains(currentUserUid)){
                    Integer likeCount1 = post1.getLikeCount();
                    post1.removeLike(currentUserUid);
                    postRef.setValue(post1);
                    holder.getLikeCount().setText(likeCount1.toString());
                } else {
                    Integer likeCount1 = post1.getLikeCount();
                    post1.addLike(currentUserUid);
                    postRef.setValue(post1);
                    holder.getLikeCount().setText(likeCount1.toString());
                }
            });
        });

        holder.getCommentButton().setOnClickListener(v -> {
            String postId = post.getPostId();
            Intent intent = new Intent(v.getContext(), ShowCommentsActivity.class);
            intent.putExtra("postId", postId);
            v.getContext().startActivity(intent);
        });

        ImageView test = holder.getUserProfilePicture();
        test.setOnClickListener(v -> Toast.makeText(holder.itemView.getContext(), post.getPictureUrlList().get(0), Toast.LENGTH_SHORT).show());
    }

    public void deletePost(String postId) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("posts")
                .child(postId);

        postRef.get().addOnSuccessListener(dataSnapshot -> {
            ClassicPost post = dataSnapshot.getValue(ClassicPost.class);
            String commentId = post.getCommentsId();
            if (!commentId.equals("")) {
                DatabaseReference commentsRef = FirebaseDatabase
                        .getInstance(DATABASE_URL)
                        .getReference("commentLists")
                        .child(commentId);
                commentsRef.removeValue();
            }

            if (post.getPictureUrlList() != null) {
                for (int i=0; i<post.getPictureUrlList().size(); i++) {
                    FirebaseStorage.getInstance().getReferenceFromUrl(post.getPictureUrlList().get(i)).delete();
                }
            }

            if (videoUrl != null) {
                FirebaseStorage.getInstance().getReferenceFromUrl(videoUrl).delete();
            }
        });
        postRef.removeValue();
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

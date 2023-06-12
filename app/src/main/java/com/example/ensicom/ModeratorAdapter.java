package com.example.ensicom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class ModeratorAdapter extends RecyclerView.Adapter<ModeratorHolder> {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/";

    List<ClassicPost> postsList;
    List<String> pictureUrlList = new ArrayList<>();
    String videoUrl;

    public ModeratorAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public ModeratorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_and_post_item, parent, false);
        return new ModeratorHolder(view);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ModeratorHolder holder, int position) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int currentPosition = position;
        ClassicPost post = postsList.get(position);

        String postContent = post.getContent();
        pictureUrlList = post.getPictureUrlList();
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
            holder.getRefusePostButton().setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getRefusePostButton().getContext());
                builder.setTitle("Refuser le post");
                builder.setMessage("Êtes-vous sûr de vouloir refuser ce post ?");
                builder.setPositiveButton("Oui", (dialog, which) -> {
                    DatabaseReference postRef = FirebaseDatabase
                            .getInstance(DATABASE_URL)
                            .getReference("moderationPost")
                            .child(post.getPostId());
                    postRef.removeValue();
                    Toast.makeText(holder.getRefusePostButton().getContext(), "Post refusé", Toast.LENGTH_SHORT).show();
                    postsList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, postsList.size());
                });
                builder.setNegativeButton("Non", (dialog, which) -> {
                    Toast.makeText(holder.getRefusePostButton().getContext(), "Post non refusé", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            });
            holder.getValidatePostButton().setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getValidatePostButton().getContext());
                builder.setTitle("Valider le post");
                builder.setMessage("Êtes-vous sûr de vouloir valider ce post ?");
                builder.setPositiveButton("Oui", (dialog, which) -> {
                    DatabaseReference postRef = FirebaseDatabase
                            .getInstance(DATABASE_URL)
                            .getReference("moderationPost")
                            .child(post.getPostId());
                    postRef.removeValue();
                    DatabaseReference postRef2 = FirebaseDatabase
                            .getInstance(DATABASE_URL)
                            .getReference("posts")
                            .child(post.getPostId());
                    postRef2.setValue(post);
                    Toast.makeText(holder.getValidatePostButton().getContext(), "Post validé", Toast.LENGTH_SHORT).show();
                    postsList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, postsList.size());
                });
                builder.setNegativeButton("Non", (dialog, which) -> {
                    Toast.makeText(holder.getValidatePostButton().getContext(), "Post non validé", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            });
            holder.getDeletePostButton().setVisibility(View.GONE);
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
            if (pictureUrlList != null) {
                Glide.with(holder.getPostPicture1().getContext()).load(pictureUrlList.get(0)).into(holder.getPostPicture1());
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
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

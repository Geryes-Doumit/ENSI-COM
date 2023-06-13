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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
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
    Toolbar toolbar;

    public ModeratorAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public ModeratorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_moderation_item, parent, false);
        return new ModeratorHolder(view);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ModeratorHolder holder, int position) {
        int currentPosition = position;
        ClassicPost post = postsList.get(position);

        String postContent = post.getContent();
        pictureUrlList = post.getPictureUrlList();
        videoUrl = post.getVideoUrl();
        List<String> tags = post.getTagsList();
//        toolbar = holder.getToolbar();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("user")
                .child(post.getUserId());
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            User postUser = dataSnapshot.getValue(User.class);
            String postUserName = postUser.getUsername();
            String profilePictureUrl = postUser.getProfilePicture();
            holder.getRefusePostButton().setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.getRefusePostButton().getContext());
                builder.setTitle("Refuser le post");
                builder.setMessage("Êtes-vous sûr de vouloir refuser ce post ?");
                builder.setPositiveButton("Oui", (dialog, which) -> {
                    DatabaseReference postRef = FirebaseDatabase
                            .getInstance(DATABASE_URL)
                            .getReference("moderationPost")
                            .child(post.getInvertedDate().toString())
                            .child(post.getPostId());
                    postRef.removeValue();
                    Snackbar.make(v, "Post refusé", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
                            .child(post.getInvertedDate().toString())
                            .child(post.getPostId());
                    postRef.removeValue();
                    post.setDate(System.currentTimeMillis());
                    DatabaseReference postRef2 = FirebaseDatabase
                            .getInstance(DATABASE_URL)
                            .getReference("posts")
                            .child(post.getInvertedDate().toString())
                            .child(post.getPostId());
                    postRef2.setValue(post);
                    Snackbar.make(v, "Post validé", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    postsList.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, postsList.size());
                });
                builder.setNegativeButton("Non", (dialog, which) -> {
                    Toast.makeText(holder.getValidatePostButton().getContext(), "Post non validé", Toast.LENGTH_SHORT).show();
                });
                builder.show();
            });
            holder.getUserName().setText(postUserName);
            holder.getPostContent().setText(postContent);
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
                holder.getVideoPlayer().setVisibility(View.VISIBLE);
                holder.getVideoPlayer().setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                    intent.putExtra("videoUrl", videoUrl);
                    v.getContext().startActivity(intent);
                });

            } else {
                holder.getVideoPlayer().setVisibility(View.GONE);
            }
        });
    }
    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

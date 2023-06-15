package com.example.ensicom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ensicom.ClassicPost;
import com.example.ensicom.R;
import com.example.ensicom.ShowCommentsActivity;
import com.example.ensicom.User;
import com.example.ensicom.UserAndPostViewHolder;
import com.example.ensicom.VideoPlayer;
import com.example.ensicom.ProfileHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileHolder> {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/";

    List<ClassicPost> postsList;
    User currentUser;

    public ProfileAdapter(List<ClassicPost> postsList) {
        this.postsList = postsList;
    }


    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_and_post_item, parent, false);
        return new ProfileHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ProfileHolder holder, int position) {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        int currentPosition = position;
        ClassicPost post = postsList.get(position);
        FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("user")
                .child(currentUserUid)
                .get()
                .addOnSuccessListener(dataSnapshot -> currentUser = dataSnapshot.getValue(User.class));

        String postContent = post.getContent();
        Integer likeCount = post.getLikeCount();
        String videoUrl = post.getVideoUrl();
        List<String> tags = post.getTagsList();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("user")
                .child(post.getUserId());
        userRef.get().addOnSuccessListener(dataSnapshot -> {
            User postUser = dataSnapshot.getValue(User.class);
            String postUserName = postUser.getUsername();
            String profilePictureUrl = postUser.getProfilePicture();
            String postId = post.getPostId();
            String postInvertedDate = post.getInvertedDate().toString();
            if (currentUser.isAdmin()) {
                holder.getDeletePostButton().setVisibility(View.VISIBLE);
            } else {
                holder.getDeletePostButton().setVisibility(View.GONE);
            }

            holder.getDeletePostButton().setOnClickListener(v -> new AlertDialog.Builder(v.getContext()).setTitle("Supprimer le post")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce post ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        deletePost(postId, postInvertedDate);
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
            if (tag.equals("")) {
                holder.getTagList().setVisibility(View.GONE);
            }
            if (post.getPictureUrlList() != null) {
                holder.getMainImageLayout().setVisibility(View.VISIBLE);
                Glide.with(holder.getPostPicture1().getContext()).load(post.getPictureUrlList().get(0)).into(holder.getPostPicture1());
            }
            else {
                holder.getMainImageLayout().setVisibility(View.GONE);
            }
            if (videoUrl != null) {
                holder.getMainVideoLayout().setVisibility(View.VISIBLE);
                holder.getPlayVideo().setVisibility(View.VISIBLE);
                Glide.with(holder.getPlayVideo().getContext()).load(videoUrl).into(holder.getPlayVideo());
                holder.getPlayVideo().setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), VideoPlayer.class);
                    intent.putExtra("videoUrl", videoUrl);
                    v.getContext().startActivity(intent);
                });
            } else {
                holder.getPlayVideo().setVisibility(View.GONE);
            }
            Button like_button1 = holder.itemView.findViewById(R.id.likeButton);
            if (post.getLikeUserList().contains(currentUserUid)) {
                like_button1.setBackgroundResource(R.drawable.like_full);
            }
            else{
                like_button1.setBackgroundResource(R.drawable.like);
            }
        });
        holder.getLikeButton().setOnClickListener(view -> {
            String postId = post.getPostId();
            DatabaseReference postRef = FirebaseDatabase
                    .getInstance(DATABASE_URL)
                    .getReference("posts")
                    .child(post.getInvertedDate().toString())
                    .child(postId);
            postRef.get().addOnSuccessListener(dataSnapshot -> {
                ClassicPost post1 = dataSnapshot.getValue(ClassicPost.class);
                if (post1.getLikeUserList().toString().contains(currentUserUid)){
                    Button like_button1 = holder.itemView.findViewById(R.id.likeButton);
                    like_button1.setBackgroundResource(R.drawable.like);
                    post1.removeLike(currentUserUid);
                    postRef.setValue(post1);
                    holder.getLikeCount().setText(post1.getLikeCount().toString());
                } else {
                    Integer likeCount1 = post1.getLikeCount();
                    Button like_button1 = holder.itemView.findViewById(R.id.likeButton);
                    like_button1.setBackgroundResource(R.drawable.like_full);
                    post1.addLike(currentUserUid);
                    postRef.setValue(post1);
                    holder.getLikeCount().setText(post1.getLikeCount().toString());
                }
            });
        });

        holder.getCommentButton().setOnClickListener(v -> {
            String postId = post.getPostId();
            Intent intent = new Intent(v.getContext(), ShowCommentsActivity.class);
            intent.putExtra("postId", postId);
            intent.putExtra("postInvertedDate", post.getInvertedDate().toString());
            v.getContext().startActivity(intent);
        });
    }

    public void deletePost(String postId, String postInvertedDate) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference("posts")
                .child(postInvertedDate)
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
            if (post.getVideoUrl() != null) {
                FirebaseStorage.getInstance().getReferenceFromUrl(post.getVideoUrl()).delete();
            }
        });
        postRef.removeValue();
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

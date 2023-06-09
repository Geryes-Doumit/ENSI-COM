package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ShowCommentsActivity extends AppCompatActivity {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String ILIA = "Il y a ";
    public static final String COMMENTAIRES = " commentaire(s) sous ce post.";
    public static final String COMMENT_LIST = "commentList";
    public static final String POSTS = "posts";
    private String postId;
    private FirebaseUser currentUser;
    private RecyclerView commentsRecyclerView;
    private List<Comment> commentsList = new ArrayList<>();
    private TextView commentCount;
    private TextView postContent;
    private TextView userName;
    private ImageButton deletePostButton;
    private ImageView userProfilePicture;
    private ImageView imageContent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageButton sendCommentButton;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        postId = getIntent().getStringExtra("postId");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentCount = findViewById(R.id.commentCount);

        FirebaseDatabase database = FirebaseDatabase
                .getInstance(DATABASE_URL);
        DatabaseReference postRef = database.getReference(POSTS).child(postId);

        postRef.get().addOnSuccessListener(dataSnapshot -> {
            ClassicPost post = dataSnapshot.getValue(ClassicPost.class);

            // Showing the post information
            postContent = findViewById(R.id.postContent);
            postContent.setText(post.getContent());

            userName = findViewById(R.id.userName);
            // Get the user name
            DatabaseReference userRef = database.getReference("user").child(post.getUserId());
            userRef.get().addOnSuccessListener(dataSnapshot1 -> {
                User user = dataSnapshot1.getValue(User.class);
                String username = user.getUsername();
                userName.setText(username);

                deletePostButton = findViewById(R.id.deletePostButton);
                if (!currentUser.getUid().equals(post.getUserId())) {
                    deletePostButton.setVisibility(View.GONE);
                } else {
                    deletePostButton.setVisibility(View.VISIBLE);
                }

                deletePostButton.setOnClickListener(v -> deletePost(postId));

                userProfilePicture = findViewById(R.id.userProfilePicture);
                Glide.with(userProfilePicture.getContext()).load(user.getProfilePicture()).into(userProfilePicture);

                imageContent1 = findViewById(R.id.imageContent1);
                Glide.with(imageContent1.getContext()).load(post.getPictureUrl1()).into(imageContent1);
            });

            // Showing the comments
            if (post.getCommentId().equals("")) {
                commentCount.setText("Il n'y a aucun commentaire sous ce post.");
                return;
            }

            database.getReference(COMMENT_LIST)
                    .child(post.getCommentId())
                    .child("comments")
                    .limitToLast(50)
                    .get()
                    .addOnSuccessListener(dataSnapshot12 -> {
                        commentsList.clear();
                        for (DataSnapshot commentSnapshot : dataSnapshot12.getChildren()) {
                            Comment comment = commentSnapshot.getValue(Comment.class);
                            commentsList.add(comment);
                        }
                        Collections.reverse(commentsList);
                        commentCount.setText(ILIA + post.getCommentCount().toString() + COMMENTAIRES);
                        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
                    });
        }).addOnFailureListener(e -> Toast.makeText(ShowCommentsActivity.this, "Erreur lors du chargement des commentaires.", Toast.LENGTH_SHORT).show());

        sendCommentButton = findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(v -> sendComment());
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void sendComment() {
        TextView commentContent;
        commentContent = findViewById(R.id.newCommentContent);
        String content = commentContent.getText().toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // get the post from the database
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(DATABASE_URL);
        DatabaseReference postRef = database.getReference(POSTS).child(postId);

        postRef.get().addOnSuccessListener(dataSnapshot -> {
            ClassicPost post = dataSnapshot.getValue(ClassicPost.class);

            if (post.getCommentId().equals("")) {
                CommentsList commentsListObject = new CommentsList("");
                commentsListObject.setCommentsListId(database.getReference().child(COMMENT_LIST).push().getKey());
                post.setCommentId(commentsListObject.getCommentsListId());
                Integer commentId = commentsListObject.getComments().get(commentsListObject.getComments().size()-1).getCommentId()+1;
                Comment comment = new Comment(currentUser.getUid(), commentId, content, new Date().getTime());
                commentsListObject.addComment(comment);

                DatabaseReference commentsListRef = database.getReference().child(COMMENT_LIST)
                        .child(commentsListObject.getCommentsListId());
                commentsListRef.setValue(commentsListObject).addOnSuccessListener(unused -> {
                    post.setCommentCount(post.getCommentCount() + 1);
                    postRef.setValue(post);
                    commentsListRef.get().addOnSuccessListener(dataSnapshot1 -> {
                        CommentsList commentsListObject1 = dataSnapshot1.getValue(CommentsList.class);
                        commentsList = commentsListObject1.getComments();
                        Collections.reverse(commentsList);
                        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
                        commentCount.setText(ILIA + post.getCommentCount().toString() + COMMENTAIRES);
                        commentContent.setText("");
                        Toast.makeText(ShowCommentsActivity.this, "Commentaire envoyé.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e ->
                            Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi du commentaire.", Toast.LENGTH_SHORT).show());
                });
                commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
            }
            else {
                DatabaseReference commentsListRef = database.getReference("commentLists")
                        .child(post.getCommentId());
                commentsListRef.get().addOnSuccessListener(dataSnapshot12 -> {
                    CommentsList commentsListObject = dataSnapshot12.getValue(CommentsList.class);
                    Integer commentId = commentsListObject.getComments().get(commentsListObject.getComments().size()-1).getCommentId()+1;
                    Comment comment = new Comment(currentUser.getUid(), commentId, content, new Date().getTime());
                    commentsListObject.addComment(comment);
                    commentsListRef.setValue(commentsListObject).addOnSuccessListener(unused -> {
                        post.setCommentCount(post.getCommentCount() + 1);
                        postRef.setValue(post);
                        commentCount.setText(ILIA + post.getCommentCount().toString() + COMMENTAIRES);
                        commentsListRef.child("comments").limitToLast(50).get().addOnSuccessListener(dataSnapshot121 -> {
                            commentsList.clear();
                            for (DataSnapshot commentSnapshot : dataSnapshot121.getChildren()) {
                                Comment comment1 = commentSnapshot.getValue(Comment.class);
                                commentsList.add(comment1);
                            }
                            Collections.reverse(commentsList);
                            commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
                            commentContent.setText("");
                            Toast.makeText(ShowCommentsActivity.this, "Commentaire envoyé.", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e ->
                                Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi du commentaire.", Toast.LENGTH_SHORT).show());
                    });
                });
            }
        });
    }

    public void deletePost(String postId) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference(POSTS)
                .child(postId);

        postRef.child("commentId").get().addOnSuccessListener(dataSnapshot -> {
            String commentId = dataSnapshot.getValue(String.class);
            DatabaseReference commentsRef = FirebaseDatabase
                    .getInstance(DATABASE_URL)
                    .getReference("commentLists")
                    .child(commentId);
            commentsRef.removeValue();
        });
        postRef.removeValue().addOnSuccessListener(unused -> {
        }).addOnFailureListener(e -> {
        });

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
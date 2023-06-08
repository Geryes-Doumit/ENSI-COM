package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowCommentsActivity extends AppCompatActivity {
    private String postId;
    private FirebaseUser currentUser;
    private ImageButton sendCommentButton;
    private TextView commentContent;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        postId = getIntent().getStringExtra("postId");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentCount = findViewById(R.id.commentCount);

        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference postRef = database.getReference("posts").child(postId);

        postRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ClassicPost post = dataSnapshot.getValue(ClassicPost.class);

                // Showing the post information
                postContent = findViewById(R.id.postContent);
                postContent.setText(post.getContent());

                userName = findViewById(R.id.userName);
                // Get the user name
                DatabaseReference userRef = database.getReference("user").child(post.getUserId());
                userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String username = user.getUsername();
                        userName.setText(username);

                        deletePostButton = findViewById(R.id.deletePostButton);
                        if (!currentUser.getUid().equals(post.getUserId())) {
                            deletePostButton.setVisibility(ImageButton.GONE);
                        } else {
                            deletePostButton.setVisibility(ImageButton.VISIBLE);
                        }

                        // TODO: add the onClickListener to delete the post

                        userProfilePicture = findViewById(R.id.userProfilePicture);
                        Glide.with(userProfilePicture.getContext()).load(user.getProfilePicture()).into(userProfilePicture);

                        imageContent1 = findViewById(R.id.imageContent1);
                        Glide.with(imageContent1.getContext()).load(post.getPictureUrl1()).into(imageContent1);
                    }
                });

                commentsList = post.getCommentsList();
                Collections.reverse(commentsList);
                commentCount.setText("Il y a " + post.getCommentCount().toString() + " commentaire(s) sous ce post.");
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShowCommentsActivity.this, "Erreur lors du chargement des commentaires.", Toast.LENGTH_SHORT).show();
            }
        });

        sendCommentButton = findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(v -> sendComment());
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void sendComment() {
        commentContent = findViewById(R.id.newCommentContent);
        String content = commentContent.getText().toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // get the post from the database
        FirebaseDatabase database = FirebaseDatabase
                .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference postRef = database.getReference("posts").child(postId);

        postRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ClassicPost post = dataSnapshot.getValue(ClassicPost.class);
                post.addComment(new Comment(content, currentUser.getUid(), post.getCommentCount()+1));
                postRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        commentCount.setText("Il y a " + post.getCommentCount().toString() + " commentaire(s) sous ce post.");
                        commentsList = post.getCommentsList();
                        Collections.reverse(commentsList);
                        commentsRecyclerView.setAdapter(new CommentsRecyclerAdapter(commentsList));
                        commentContent.setText("");
                        Toast.makeText(ShowCommentsActivity.this, "Commentaire envoyé.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi du commentaire.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
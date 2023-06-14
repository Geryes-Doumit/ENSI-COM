package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    public static final String COMMENT_LISTS = "commentLists";
    public static final String POSTS = "posts";
    private String postId;
    private FirebaseUser currentUser;
    private RecyclerView commentsRecyclerView;
    private List<Comment> commentsList = new ArrayList<>();
    private CommentsRecyclerAdapter commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
    private TextView commentCount;
    private TextView postContent;
    private TextView userName;
    private ImageButton deletePostButton;
    private ImageView userProfilePicture;
    private ImageView imageContent1;
    private ImageView videoImageView;
    private ImageButton sendCommentButton;
    private EditText commentContent;
    private String postInvertedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comments);

        postId = getIntent().getStringExtra("postId");
        postInvertedDate = getIntent().getStringExtra("postInvertedDate");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentCount = findViewById(R.id.commentCount);
        videoImageView = findViewById(R.id.videoImageView);
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseDatabase database = FirebaseDatabase
                .getInstance(DATABASE_URL);
        DatabaseReference postRef = database.getReference(POSTS).child(postInvertedDate).child(postId);

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

                deletePostButton.setOnClickListener(v -> deletePost(postId, postInvertedDate));

                userProfilePicture = findViewById(R.id.userProfilePicture);
                Glide.with(userProfilePicture.getContext()).load(user.getProfilePicture()).circleCrop().into(userProfilePicture);

                imageContent1 = findViewById(R.id.imageContent1);
                List<String> urlList = post.getPictureUrlList();
                if (urlList != null) {
                    Glide.with(imageContent1.getContext()).load(post.getPictureUrlList().get(0)).into(imageContent1);
                }
                if (urlList == null || urlList.isEmpty()) {
                    imageContent1.setVisibility(View.GONE);
                } else {
                    imageContent1.setVisibility(View.VISIBLE);
                }
                String videoUrl = post.getVideoUrl();
                if (!(videoUrl ==null)) {
                    Glide.with(videoImageView.getContext()).load(videoUrl).into(videoImageView);
                    videoImageView.setOnClickListener(v -> {
                        Intent intent = new Intent(ShowCommentsActivity.this, VideoPlayer.class);
                        intent.putExtra("videoUrl", videoUrl);
                        startActivity(intent);
                    });
                }
            });

            // Showing the comments
            if (post.getCommentsId().equals("")) {
                commentCount.setText("Il n'y a aucun commentaire sous ce post.");
                return;
            }

            database.getReference(COMMENT_LISTS)
                    .child(post.getCommentsId())
                    .child("comments")
                    .limitToLast(50)
                    .get()
                    .addOnSuccessListener(dataSnapshot12 -> {
                        if (dataSnapshot12.exists()) {
                            commentsList.clear();
                            for (DataSnapshot commentSnapshot : dataSnapshot12.getChildren()) {
                                Comment comment = commentSnapshot.getValue(Comment.class);
                                commentsList.add(comment);
                            }
                            commentsList.remove(0);
                            Collections.reverse(commentsList);
                            post.setCommentCount(commentsList.size());
                            postRef.setValue(post);
                            commentCount.setText(ILIA + post.getCommentCount().toString() + COMMENTAIRES);
                            commentsRecyclerAdapter.notifyDataSetChanged();
                        }
                        else {
                            post.setCommentsId("");
                            post.setCommentCount(0);
                            postRef.setValue(post);
                        }
                    });
        }).addOnFailureListener(e -> Toast.makeText(ShowCommentsActivity.this, "Erreur lors du chargement des commentaires.", Toast.LENGTH_SHORT).show());

        commentContent = findViewById(R.id.newCommentContent);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(v -> {
            commentContent.clearFocus();
            if (Boolean.TRUE.equals(commentsRecyclerAdapter.getReplyMode())) {
                sendReply();
            } else {
                sendComment();
            }
        });
    }

    public void sendComment() {
        TextView commentContent;
        commentContent = findViewById(R.id.newCommentContent);
        String content = commentContent.getText().toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Vous ne pouvez pas envoyer de commentaire vide.", Toast.LENGTH_SHORT).show();
            return;
        }

        // get the post from the database
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(DATABASE_URL);
        DatabaseReference postRef = database.getReference(POSTS).child(postInvertedDate).child(postId);

        postRef.get().addOnSuccessListener(dataSnapshot -> {

            if (!dataSnapshot.exists()) {
                Toast.makeText(this, "Ce post n'existe plus.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            ClassicPost post = dataSnapshot.getValue(ClassicPost.class);

            if (post.getCommentsId().equals("")) {
                CommentsList commentsListObject = new CommentsList("");
                commentsListObject.setCommentsListId(database.getReference().child(COMMENT_LISTS).push().getKey());
                post.setCommentsId(commentsListObject.getCommentsListId());
                Integer commentId = commentsListObject.getComments().get(commentsListObject.getComments().size()-1).getCommentPosition()+1;
                Comment comment = new Comment(currentUser.getUid(), commentId, content, new Date().getTime());
                comment.setCommentsListId(commentsListObject.getCommentsListId());
                commentsListObject.addComment(comment);

                DatabaseReference commentsListRef = database.getReference().child(COMMENT_LISTS)
                        .child(commentsListObject.getCommentsListId());
                commentsListRef.setValue(commentsListObject).addOnSuccessListener(unused -> {
                    post.setCommentCount(post.getCommentCount() + 1);
                    postRef.setValue(post);
                    commentsListRef.child("comments").get().addOnSuccessListener(dataSnapshot1 -> {
                        commentsList.clear();
                        for (DataSnapshot dS : dataSnapshot1.getChildren()) {
                            Comment com = dS.getValue(Comment.class);
                            commentsList.add(com);
                        }
                        commentsList.remove(0);
                        Collections.reverse(commentsList);
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        commentCount.setText(ILIA + post.getCommentCount().toString() + COMMENTAIRES);
                        commentContent.setText("");
                        Toast.makeText(ShowCommentsActivity.this, "Commentaire envoyé.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e ->
                        Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi du commentaire.", Toast.LENGTH_SHORT).show());
                });
            }
            else {
                DatabaseReference commentsListRef = database.getReference(COMMENT_LISTS)
                        .child(post.getCommentsId());
                commentsListRef.get().addOnSuccessListener(dataSnapshot12 -> {
                    if (!dataSnapshot12.exists()) {
                        post.setCommentsId("");
                        post.setCommentCount(0);
                        postRef.setValue(post);
                        sendComment();
                        return;
                    }
                    CommentsList commentsListObject = dataSnapshot12.getValue(CommentsList.class);
                    Integer commentId = commentsListObject.getComments().get(commentsListObject.getComments().size()-1).getCommentPosition()+1;
                    Comment comment = new Comment(currentUser.getUid(), commentId, content, new Date().getTime());
                    comment.setCommentsListId(commentsListObject.getCommentsListId());
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
                            commentsList.remove(0);
                            Collections.reverse(commentsList);
                            commentsRecyclerAdapter.notifyDataSetChanged();
                            commentContent.setText("");
                            Toast.makeText(ShowCommentsActivity.this, "Commentaire envoyé.", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e ->
                                Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi du commentaire.", Toast.LENGTH_SHORT).show());
                    });
                });
            }
        });
    }
    private void sendReply() {
        TextView commentContent;
        commentContent = findViewById(R.id.newCommentContent);
        String content = commentContent.getText().toString();

        if (content.isEmpty()) {
            Toast.makeText(this, "Vous ne pouvez pas envoyer de commentaire vide", Toast.LENGTH_SHORT).show();
            return;
        }
        Comment comment = commentsRecyclerAdapter.getReplyComment();

        // get the post from the database
        FirebaseDatabase database = FirebaseDatabase
                .getInstance(DATABASE_URL);
        DatabaseReference postRef = database
                .getReference(POSTS)
                .child(postInvertedDate)
                .child(postId);

        postRef.get().addOnSuccessListener(dataSnapshot -> {
            ClassicPost post = dataSnapshot.getValue(ClassicPost.class);
            DatabaseReference commentRef = database
                  .getReference(COMMENT_LISTS)
                  .child(post.getCommentsId())
                  .child("comments")
                  .child(comment.getCommentPosition().toString());

            if (comment.getRepliesId().equals("")) {
                CommentReply reply = new CommentReply(content, currentUser.getUid(), 1);
                CommentRepliesList commentRepliesList = new CommentRepliesList("");

                commentRepliesList.setRepliesListId(database.getReference("replyLists").push().getKey());
                commentRepliesList.addReply(reply);
                comment.setRepliesId(commentRepliesList.getRepliesListId());
                commentRef.setValue(comment).addOnSuccessListener(unused -> {
                    database.getReference("replyLists").child(commentRepliesList.getRepliesListId()).setValue(commentRepliesList);
                    commentContent.setText("");
                    Toast.makeText(ShowCommentsActivity.this, "Réponse envoyée.", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e ->
                    Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi de la réponse.", Toast.LENGTH_SHORT).show());
            }
            else {
                DatabaseReference repliesListRef = database.getReference("replyLists").child(comment.getRepliesId());
                repliesListRef.get().addOnSuccessListener(dataSnapshot1 -> {
                    CommentRepliesList commentRepliesList = dataSnapshot1.getValue(CommentRepliesList.class);
                    Integer replyId = commentRepliesList.getReplies().get(commentRepliesList.getReplies().size()-1).getReplyPosition()+1;
                    CommentReply reply = new CommentReply(content, currentUser.getUid(), replyId);
                    commentRepliesList.addReply(reply);
                    repliesListRef.setValue(commentRepliesList).addOnSuccessListener(unused -> {
                        commentContent.setText("");
                        Toast.makeText(ShowCommentsActivity.this, "Réponse envoyée.", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e ->
                        Toast.makeText(ShowCommentsActivity.this, "Erreur lors de l'envoi de la réponse.", Toast.LENGTH_SHORT).show());
                });
            }
        });
        commentsRecyclerAdapter.notifyDataSetChanged();
        commentsRecyclerAdapter.setReplyMode(false);
        EditText commentEditText = findViewById(R.id.newCommentContent);
        commentEditText.setHint("Ajouter un commentaire...");
    }

    public void deletePost(String postId, String postInvertedDate) {
        DatabaseReference postRef = FirebaseDatabase
                .getInstance(DATABASE_URL)
                .getReference(POSTS)
                .child(postInvertedDate)
                .child(postId);

        postRef.child("commentId").get().addOnSuccessListener(dataSnapshot -> {
            String commentId = dataSnapshot.getValue(String.class);
            DatabaseReference commentsRef = FirebaseDatabase
                    .getInstance(DATABASE_URL)
                    .getReference(COMMENT_LISTS)
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
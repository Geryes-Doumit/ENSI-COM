package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

public class NewPostActivity extends AppCompatActivity {
    EditText textContainer;
    Button postButton;
    ImageView pictureToPost;
    private Uri imagePath;
    String profilePictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pictureToPost=findViewById(R.id.imageViewPostPicture);
        pictureToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pictureIntent= new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent,1);
            }
        });


        textContainer = findViewById(R.id.postContent);
        postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String content = textContainer.getText().toString();

                if (user == null) {
                    Toast.makeText(NewPostActivity.this, "Vous devez être connecté pour poster un contenu.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (content.isEmpty()) {
                    Toast.makeText(NewPostActivity.this, "Veuillez entrer un contenu.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imagePath==null) {
                    Post();
                }
                else {
                    uploadPost();
                }
            }
        });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imagePath=data.getData();
            getImageinImageView();
        }
    }
    private void getImageinImageView(){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
        } catch (IOException e) {
            Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        pictureToPost.setImageBitmap(bitmap);
    }

    private void uploadPost() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement...");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference("images"+ UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        profilePictureUrl = task.getResult().toString();
                                        Post();
                                    }
                                }
                            });
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(NewPostActivity.this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = 100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Chargement "+(int)progress+"%");
                    }
                });

    }
    private void Post() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String content = textContainer.getText().toString();

        ClassicPost post = new ClassicPost(content, user.getUid(), new Date().getTime(), profilePictureUrl);
        DatabaseReference postsRef = ref.child("posts");
        postsRef.push().setValue(post).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewPostActivity.this, "Une erreur est survenue, veuillez réessayer.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(NewPostActivity.this, "Votre contenu a été posté.", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(NewPostActivity.this, HomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });
    }
}
package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class SetupProfileActivity extends AppCompatActivity {
    EditText nameSettings;
    Button updateButton;
    ImageView profilePicture;
    private Uri imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        nameSettings=findViewById(R.id.editTextUpdateProfileNameSettings);
        updateButton=findViewById(R.id.buttonUpdateProfileSettings);
        profilePicture=findViewById(R.id.imageViewPostPicture);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= nameSettings.getText().toString();
                if (nameSettings.getText().toString().isEmpty()) {
                    Toast.makeText(SetupProfileActivity.this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imagePath!=null && !nameSettings.getText().toString().isEmpty()){
                    uploadImage();
                    Toast.makeText(SetupProfileActivity.this, "Profil créé", Toast.LENGTH_SHORT).show();
                }
                if (!nameSettings.getText().toString().isEmpty()) {
                    updateProfileName(name);
                    Toast.makeText(SetupProfileActivity.this, "Profil créé", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(SetupProfileActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                Toast.makeText(SetupProfileActivity.this, "Le profil n'a pas pu être crée.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        });
        profilePicture=findViewById(R.id.imageViewPostPicture);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pictureIntent= new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent,1);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imagePath=data.getData();
            getImageInImageView();
        }
    }
    private void getImageInImageView(){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            profilePicture.setImageBitmap(bitmap);
        } else {
            profilePicture.setImageResource(R.drawable.ic_launcher_foreground);
            Toast.makeText(this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
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
                                        updateProfilePicture(task.getResult().toString());
                                    }
                                }
                            });
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SetupProfileActivity.this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
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
    private void updateProfilePicture(String url){
        FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app").
                getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePicture")
                .setValue(url);
    }
    private void updateProfileName(String name){
        FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app").
                getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/username")
                .setValue(name);
    }

}
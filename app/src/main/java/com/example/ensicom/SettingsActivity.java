package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app";
    Button updateButton;
    EditText nameSettings;
    TextView userName;
    ImageView profilePicture;
    private Uri imagePath;
    String profilePictureUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        userName=findViewById(R.id.textView_settingsName);
        userName.setText(name);
        nameSettings=findViewById(R.id.editTextUpdateProfileNameSettings);
        profilePicture=findViewById(R.id.imageViewPostPicture);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance(DATABASE_URL)
                .getReference().child("user").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    profilePictureUrl = dataSnapshot.child("profilePicture").getValue(String.class);
                    Glide.with(SettingsActivity.this)
                            .load(profilePictureUrl).circleCrop()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(profilePicture);
                    if (profilePictureUrl == null) {
                        Toast.makeText(SettingsActivity.this, "Impossible de charger l'image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingsActivity.this, "L'image n'a pas pu être récupérée", Toast.LENGTH_SHORT).show();
            }
        });


        updateButton=findViewById(R.id.buttonUpdateProfileSettings);
        updateButton.setOnClickListener(v -> {
            String name1 = nameSettings.getText().toString();
            if (imagePath!=null){
                uploadImage();
                Toast.makeText(SettingsActivity.this, "Profil mis à jour", Toast.LENGTH_SHORT).show();
            }
            if (!nameSettings.getText().toString().isEmpty()) {
                updateProfileName(name1);
                Toast.makeText(SettingsActivity.this, "Profil mis à jour", Toast.LENGTH_SHORT).show();
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name1)
                        .build();
                user1.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        userName.setText(name1);
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "Le profil n'a pas pu être mis à jour", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        profilePicture=findViewById(R.id.imageViewPostPicture);
        profilePicture.setOnClickListener(v -> {
            Intent pictureIntent= new Intent(Intent.ACTION_PICK);
            pictureIntent.setType("image/*");
            startActivityForResult(pictureIntent,1);
        });
    }

    /**
     * @deprecated 
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    @Deprecated
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            imagePath=data.getData();
            getImageInImageView();
        }
    }
    private void getImageInImageView(){
        Glide .with(this)
                .load(imagePath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(profilePicture);
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chargement...");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference("images"+ UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                updateProfilePicture(task1.getResult().toString());
                            }
                        });
                        progressDialog.dismiss();
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(snapshot -> {
                    double progress = (double)100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount();
                    progressDialog.setMessage("Chargement "+(int)progress+"%");
                });

    }
    private void updateProfilePicture(String url){
        FirebaseDatabase.getInstance(DATABASE_URL).
                getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profilePicture")
                .setValue(url);
    }
    private void updateProfileName(String name){
        FirebaseDatabase.getInstance(DATABASE_URL).
                getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/username")
                .setValue(name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
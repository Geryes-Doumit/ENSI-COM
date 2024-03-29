package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.Manifest;

public class NewPostActivity extends AppCompatActivity {
    public static final String CHARGEMENT_PROGRESS_BAR_STRING = "Chargement ";
    public static final String CHARGEMENT_STRING = "Chargement...";
    public static final String CHARGEMENT_IMAGEVIEW_STRING = "Erreur lors du chargement de l'image";
    public static final String IMAGE_STRING = "images";
    EditText textContainer;
    EditText tagsContainer;
    Button postButton;
    List<String> pictureUrlList= new ArrayList<>();
    List<Uri> pictureUriList= new ArrayList<>();
    List<String> tagsList = new ArrayList<>();
    Button addVideo;
    Button addPicture;
    ImageView videoThumbnail;
    LinearLayout linearLayout;
    Uri videoUri;
    String videoUrl;
    String tags;
    String videoThumbnailUrl;
    private static final int CAMERA_AND_STORAGE_REQUEST = 100;
    private static final int STORAGE_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        if (!checkStoragePermission() && !checkCameraPermission()) {
            requestCameraAndStoragePermissions();
        }
        if (!checkStoragePermission()) {
            requestStoragePermission();
        }

        addPicture=findViewById(R.id.pictureButton);
        addVideo=findViewById(R.id.videoButton);
        linearLayout=findViewById(R.id.postLayout);
        postButton=findViewById(R.id.postButton);
        postButton.setOnClickListener(v -> {
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
            if (pictureUriList.isEmpty() && videoUri==null) {
                post();
            }
            if (videoUri!=null && pictureUriList.isEmpty()){
                uploadVideo();
            }
            if (videoUri!=null && !pictureUriList.isEmpty()){
                uploadPicture();
                uploadVideo();
            }
            else {
                uploadPicture();
            }
        });
        addPicture.setOnClickListener(v -> {
            if (!checkStoragePermission()) {
                Toast.makeText(NewPostActivity.this, "Vous devez autoriser l'accès au stockage pour pouvoir ajouter une photo.", Toast.LENGTH_SHORT).show();
                return;
            }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
        });

        addVideo.setOnClickListener(v -> {
            if (!checkStoragePermission()) {
                Toast.makeText(NewPostActivity.this, "Vous devez autoriser l'accès au stockage pour pouvoir ajouter une video.", Toast.LENGTH_SHORT).show();
                return;
            }
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, 2);
        });
        textContainer = findViewById(R.id.postContent);
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

    /**
     * @deprecated!v
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (videoThumbnail != null) {
                        linearLayout.removeView(videoThumbnail);
                    }
                    videoUri = data.getData();
                    videoThumbnail = new ImageView(this);
                    try {
                        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
                        mMMR.setDataSource(NewPostActivity.this, videoUri);
                        Glide.with(this)
                                .load(mMMR.getFrameAtTime())
                                .override(900,900)
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.ic_launcher_background)
                                        .error(R.drawable.ic_launcher_background))
                                .into(videoThumbnail);
                        linearLayout.addView(videoThumbnail);
                        videoThumbnail.setOnClickListener(v -> {
                            ViewGroup parentView = (ViewGroup) videoThumbnail.getParent();
                            if (parentView != null) {
                                parentView.removeView(videoThumbnail);
                                pictureUriList.remove(videoUri);
                            }
                        });
                    }
                    catch (Exception e) {
                        Toast.makeText(NewPostActivity.this, "Erreur lors du chargement de la vidéo", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK) {
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            ImageView imageView = new ImageView(this);
                            getImageInImageView(imageUri, imageView);
                            pictureUriList.add(imageUri);
                            linearLayout.addView(imageView);
                            imageView.setOnClickListener(v -> {
                                ViewGroup parentView = (ViewGroup) imageView.getParent();
                                if (parentView != null) {
                                    parentView.removeView(imageView);
                                    pictureUriList.remove(imageUri);
                                }
                            });
                        }
                    }
                    else {
                        if (linearLayout.getChildCount()>0) {
                            linearLayout.removeViews(0,1);
                            pictureUriList.remove(0);
                        }
                        Uri imageUri = data.getData();
                        ImageView imageView = new ImageView(this);
                        getImageInImageView(imageUri, imageView);
                        pictureUriList.add(imageUri);
                        linearLayout.addView(imageView);
                        imageView.setOnClickListener(v -> {
                            ViewGroup parentView = (ViewGroup) imageView.getParent();
                            if (parentView != null) {
                                parentView.removeView(imageView);
                                pictureUriList.remove(imageUri);
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }
    private void getImageInImageView(Uri imagePath, ImageView imageView) {
        Glide .with(NewPostActivity.this)
                .applyDefaultRequestOptions(RequestOptions.centerCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .load(imagePath)
                .override(900, 900)
                .into(imageView);
    }
    public void uploadVideo() {
        if (videoUri!=null) {
            totalTasks++;
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CHARGEMENT_STRING);
            progressDialog.show();
            FirebaseStorage.getInstance().getReference("videos" + UUID.randomUUID().toString()).putFile(videoUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    videoUrl = task1.getResult().toString();
                                    completedTasks++;
                                    checkAndUpload();
                                }
                            });
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(NewPostActivity.this, CHARGEMENT_IMAGEVIEW_STRING, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(snapshot -> {
                        double progress = (double)100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                        progressDialog.setMessage(CHARGEMENT_PROGRESS_BAR_STRING + (int) progress + "%");
                    });
        }
    }
    int totalTasks = 0;
    int completedTasks = 0;
    private void uploadPicture() {
        if (!pictureUriList.isEmpty()) {
            for (int i=0;i<pictureUriList.size();i++) {
                totalTasks++;
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle(CHARGEMENT_STRING);
                progressDialog.show();
                FirebaseStorage.getInstance().getReference(IMAGE_STRING + UUID.randomUUID().toString()).putFile(pictureUriList.get(i))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        pictureUrlList.add(task1.getResult().toString());
                                        completedTasks++;
                                        checkAndPost();
                                    }
                                });
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(NewPostActivity.this, CHARGEMENT_IMAGEVIEW_STRING, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnProgressListener(snapshot -> {
                            double progress = (double)100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                            progressDialog.setMessage(CHARGEMENT_STRING + (int) progress + "%");
                        });
            }
            }
    }
    private void checkAndPost() {
        if (completedTasks == totalTasks) {
            post();
        }
    }
    private void checkAndUpload() {
        if (completedTasks == totalTasks) {
            post();
        }
    }
    private void post() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String content = textContainer.getText().toString();

        tagsContainer = findViewById(R.id.tagsContent);
        tags=tagsContainer.getText().toString();
        String[] tagsListSplit = tags.split(";");
        tagsList.addAll(Arrays.asList(tagsListSplit));
        ClassicPost post = new ClassicPost("",content, user.getUid(), new Date().getTime(),pictureUrlList , tagsList,videoUrl);
        post.setLikeCount(0);
        DatabaseReference postsRef = ref.child("moderationPost").child(post.getInvertedDate().toString());
        String postId = postsRef.push().getKey();
        post.setPostId(postId);
        postsRef.child(postId).setValue(post).addOnFailureListener(e -> {
            postsRef.child(postId).removeValue(); // Not sure if this is necessary
            Toast.makeText(NewPostActivity.this, "Une erreur est survenue, veuillez réessayer.", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(unused -> {
            Toast.makeText(NewPostActivity.this,"Votre contenu est en cours de modération", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(NewPostActivity.this, MainActivity1.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        });
    }
    private boolean checkCameraPermission() {
        int cameraPermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storagePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return cameraPermissionResult == PackageManager.PERMISSION_GRANTED &&
                storagePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraAndStoragePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_AND_STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        int storagePermissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return storagePermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
    }

}
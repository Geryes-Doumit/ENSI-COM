package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

public class NewPostActivity extends AppCompatActivity {
    public static final String CHARGEMENT_PROGRESS_BAR_STRING = "Chargement ";
    public static final String CHARGEMENT_STRING = "Chargement...";
    public static final String CHARGEMENT_IMAGEVIEW_STRING = "Erreur lors du chargement de l'image";
    public static final String IMAGE_STRING = "images";
    EditText textContainer;
    EditText tagsContainer;
    Button postButton;
    private ArrayList<ImageView> imageViewsList = new ArrayList<>();
    private ArrayList<String> pictureUrlsList = new ArrayList<>();
    ArrayList<String> arrayTagsList = new ArrayList<>();
    ImageButton delete1;
    ImageButton delete2;
    ImageButton delete3;
    ImageButton delete4;
    ImageButton addVideo;
    ImageButton addPicture;
    ImageView pictureToPost1;
    ImageView pictureToPost2;
    ImageView pictureToPost3;
    ImageView pictureToPost4;
    ImageView pictureToPost5;
    ImageView videoThumbnail;
    Uri imagePath1;
    Uri imagePath2;
    Uri imagePath3;
    Uri imagePath4;
    Uri videoUri;
    String pictureUrl1;
    String pictureUrl2;
    String pictureUrl3;
    String pictureUrl4;
    String videoUrl;
    String tags;
    String[] tagsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pictureToPost1=findViewById(R.id.imageViewPostPicture1);
        pictureToPost2=findViewById(R.id.imageViewPostPicture2);
        pictureToPost3=findViewById(R.id.imageViewPostPicture3);
        pictureToPost4=findViewById(R.id.imageViewPostPicture4);
        pictureToPost5=findViewById(R.id.imageViewPostPicture5);
        imageViewsList.add(pictureToPost1);
        imageViewsList.add(pictureToPost2);
        imageViewsList.add(pictureToPost3);
        imageViewsList.add(pictureToPost4);
        delete1=findViewById(R.id.deleteButton1);
        delete2=findViewById(R.id.deleteButton2);
        delete3=findViewById(R.id.deleteButton3);
        delete4=findViewById(R.id.deleteButton4);
        pictureUrlsList.add(pictureUrl1);
        pictureUrlsList.add(pictureUrl2);
        pictureUrlsList.add(pictureUrl3);
        pictureUrlsList.add(pictureUrl4);
        postButton = findViewById(R.id.postButton);
        videoThumbnail = findViewById(R.id.imageViewVideoThumbnail);
        addVideo = findViewById(R.id.imageButtonVideo);
        videoThumbnail.setVisibility(View.GONE);
        addPicture = findViewById(R.id.imageButtonPicture);
        addPicture.setVisibility(View.GONE);

        addPicture.setOnClickListener(v -> {
            videoThumbnail.setVisibility(View.GONE);
            pictureToPost1.setVisibility(View.VISIBLE);
            pictureToPost2.setVisibility(View.VISIBLE);
            pictureToPost3.setVisibility(View.VISIBLE);
            pictureToPost4.setVisibility(View.VISIBLE);
            pictureToPost5.setVisibility(View.VISIBLE);
            delete1.setVisibility(View.VISIBLE);
            delete2.setVisibility(View.VISIBLE);
            delete3.setVisibility(View.VISIBLE);
            delete4.setVisibility(View.VISIBLE);
            addPicture.setVisibility(View.GONE);
        });

        addVideo.setOnClickListener(v -> {
            videoThumbnail.setVisibility(View.VISIBLE);
            addPicture.setVisibility(View.VISIBLE);
            pictureToPost1.setVisibility(View.GONE);
            pictureToPost2.setVisibility(View.GONE);
            pictureToPost3.setVisibility(View.GONE);
            pictureToPost4.setVisibility(View.GONE);
            pictureToPost5.setVisibility(View.GONE);
            delete1.setVisibility(View.GONE);
            delete2.setVisibility(View.GONE);
            delete3.setVisibility(View.GONE);
            delete4.setVisibility(View.GONE);
        });
        videoThumbnail.setOnClickListener(v -> {
            Intent videoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(videoIntent, 6);
        });

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
            if (imagePath1 == null && imagePath2 == null && imagePath3 == null && imagePath4 == null && videoUri==null) {
                post();
            }
            if (videoUri!=null){
                uploadVideo();
            }
            else {
                uploadPost();
            }
        });
        delete1.setOnClickListener(v -> {
            pictureToPost1.setImageResource(R.drawable.ic_launcher_foreground);
            imagePath1=null;
        });
        delete2.setOnClickListener(v -> {
            pictureToPost2.setImageResource(R.drawable.ic_launcher_foreground);
            imagePath2=null;
        });
        delete3.setOnClickListener(v -> {
            pictureToPost3.setImageResource(R.drawable.ic_launcher_foreground);
            imagePath3=null;
        });
        delete4.setOnClickListener(v -> {
            pictureToPost4.setImageResource(R.drawable.ic_launcher_foreground);
            imagePath4=null;
        });

        pictureToPost5.setOnClickListener(v -> {
            for (int j = 0; j < 4; j++) {
                ImageView imageViewToDelete = imageViewsList.get(j);
                imageViewToDelete.setImageResource(R.drawable.ic_launcher_foreground);
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), 5);
        });
        for (int i=0; i<4; i++) {
            ImageView imageView = imageViewsList.get(i);
            int finalI = i;
            imageView.setOnClickListener(v -> {
                Intent pictureIntent= new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent, finalI);
            });
        }


        textContainer = findViewById(R.id.postContent);
    }

    @Override
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 6:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    videoUri = data.getData();
                    String videoPath = getVideoPath(videoUri);
                    if (videoPath != null) {
                        Glide.with(NewPostActivity.this)
                                .applyDefaultRequestOptions(RequestOptions.centerCropTransform()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .load(videoPath)
                                .into(videoThumbnail);
                    } else {
                        Toast.makeText(NewPostActivity.this, "Impossible de récupérer la vidéo.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 5:
                if(resultCode == Activity.RESULT_OK) {
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        if (count > 4) {
                            Toast.makeText(NewPostActivity.this, "Vous ne pouvez pas sélectionner plus de 4 images.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<Uri> imageUris = new ArrayList<>();
                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            ImageView imageView = imageViewsList.get(i);
                            getImageInImageView(imageUri, imageView);
                            imageUris.add(imageUri);
                        }
                        if (!imageUris.isEmpty()) {
                                imagePath1 = imageUris.get(0);
                        }
                        if (imageUris.size() >= 2) {
                            imagePath2 = imageUris.get(1);
                        }
                        if (imageUris.size() >= 3) {
                            imagePath3 = imageUris.get(2);
                        }
                        if (imageUris.size() >= 4) {
                            imagePath4 = imageUris.get(3);
                        }

                    }
                    else {
                        Uri imageUri = data.getData();
                        ImageView imageView = imageViewsList.get(0);
                        getImageInImageView(imageUri, imageView);
                    }
                }
                break;
            case 0:
                if(resultCode == Activity.RESULT_OK) {
                    imagePath1 = data.getData();
                    ImageView imageView = imageViewsList.get(0);
                    getImageInImageView(imagePath1, imageView);
                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK) {
                    imagePath2 = data.getData();
                    ImageView imageView = imageViewsList.get(1);
                    getImageInImageView(imagePath2, imageView);
                }
                break;
            case 2:
                if(resultCode == Activity.RESULT_OK) {
                    imagePath3 = data.getData();
                    ImageView imageView = imageViewsList.get(2);
                    getImageInImageView(imagePath3, imageView);
                }
                break;
            case 3:
                if(resultCode == Activity.RESULT_OK) {
                    imagePath4 = data.getData();
                    ImageView imageView = imageViewsList.get(3);
                    getImageInImageView(imagePath4, imageView);
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
    private void uploadPost() {
        if (imagePath1 != null) {
            totalTasks++;
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CHARGEMENT_STRING);
            progressDialog.show();
            FirebaseStorage.getInstance().getReference(IMAGE_STRING + UUID.randomUUID().toString()).putFile(imagePath1)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    pictureUrl1 = task1.getResult().toString();
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
        if (imagePath2 != null) {
            totalTasks++;
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CHARGEMENT_STRING);
            progressDialog.show();
            FirebaseStorage.getInstance().getReference(IMAGE_STRING + UUID.randomUUID().toString()).putFile(imagePath2)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task12 -> {
                                if (task12.isSuccessful()) {
                                    pictureUrl2 = task12.getResult().toString();
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
        if (imagePath3 != null) {
            totalTasks++;
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CHARGEMENT_STRING);
            progressDialog.show();
            FirebaseStorage.getInstance().getReference(IMAGE_STRING + UUID.randomUUID().toString()).putFile(imagePath3)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task13 -> {
                                if (task13.isSuccessful()) {
                                    pictureUrl3 = task13.getResult().toString();
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
        if (imagePath4 != null) {
            totalTasks++;
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle(CHARGEMENT_STRING);
            progressDialog.show();
            FirebaseStorage.getInstance().getReference(IMAGE_STRING + UUID.randomUUID().toString()).putFile(imagePath4)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task14 -> {
                                if (task14.isSuccessful()) {
                                    pictureUrl4 = task14.getResult().toString();
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
                        progressDialog.setMessage(CHARGEMENT_PROGRESS_BAR_STRING + (int) progress + "%");
                    });
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
        tagsList = tags.split(";");
        arrayTagsList.addAll(Arrays.asList(tagsList));
        ClassicPost post = new ClassicPost("",content, user.getUid(), new Date().getTime(), pictureUrl1, pictureUrl2, pictureUrl3, pictureUrl4, arrayTagsList,videoUrl);
        post.setLikeCount(0);
        DatabaseReference postsRef = ref.child("posts");
        String postId = postsRef.push().getKey();
        post.setPostId(postId);
        postsRef.child(postId).setValue(post).addOnFailureListener(e -> {
            postsRef.child(postId).removeValue(); // Not sure if this is necessary
            Toast.makeText(NewPostActivity.this, "Une erreur est survenue, veuillez réessayer.", Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(unused -> {
            Toast.makeText(NewPostActivity.this, "Votre contenu a été posté.", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(NewPostActivity.this, HomeActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        });
    }
    private String getVideoPath(Uri videoUri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(videoUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String videoPath = cursor.getString(columnIndex);
            cursor.close();
            Toast.makeText(NewPostActivity.this, videoPath, Toast.LENGTH_SHORT).show();
            return videoPath;
        }
        return null;
    }
}
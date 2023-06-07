package com.example.ensicom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView postsListView;
    TextView userName;
    Button settings;
    Button newPostButton;
    Button testButton;
    List<String> userNameList = new ArrayList<>();
    List<String> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testButton=findViewById(R.id.buttonTest);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                //finish();
            }
        });
        List<HashMap<String, String>> itemsList = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(
                HomeActivity.this,
                itemsList,
                R.layout.user_and_post_item,
                new String[]{"Profile Picture","First Line", "Second Line"},
                new int[]{R.id.userProfilePicture, R.id.userName, R.id.postContent});

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserName = currentUser.getDisplayName();

        postsListView = findViewById(R.id.postsList);
        DatabaseReference postsRef = FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/").getReference("posts");
        postsRef.limitToLast(10).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ClassicPost post = postSnapshot.getValue(ClassicPost.class);
                    String postId = postSnapshot.getKey();
                    String postContent = post.getContent();
                    Long date = post.getDate();

                    DatabaseReference user = FirebaseDatabase
                            .getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("user")
                            .child(post.getUserId());
                    user.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            User postUser = dataSnapshot.getValue(User.class);
                            String postUserName;
                            if (postUser == null) {
                                postUserName = "Sad User";
                            } else {
                                postUserName = postUser.getUsername();
                            }
                            userNameList.add(postUserName);
                            postList.add(postContent +" liked by "+ String.valueOf(post.getLikeUserList().size())+" people");

                            HashMap<String, String> map = new HashMap<>();
                            map.put("First Line", userNameList.get(userNameList.size()-1));
                            map.put("Second Line", postList.get(postList.size()-1));
                            itemsList.add(map);

                            postsListView.setAdapter(adapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, "Une erreur est survenue, veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            userNameList.add("User Not Found");
                            postList.add(postContent);

                            HashMap<String, String> map = new HashMap<>();
                            map.put("First Line", userNameList.get(userNameList.size()-1));
                            map.put("Second Line", postList.get(postList.size()-1));
                            itemsList.add(map);

                            postsListView.setAdapter(adapter);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Une erreur est survenue, veuillez réessayer.");
            }
        });

        Toolbar toolbar=findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        userName=findViewById(R.id.textView_userName);
        userName.setText(name);
        settings=findViewById(R.id.buttonHomeSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                //finish();
            }
        });

        newPostButton=findViewById(R.id.newPost);

        newPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(HomeActivity.this, NewPostActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                //finish();
            }
        });
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}
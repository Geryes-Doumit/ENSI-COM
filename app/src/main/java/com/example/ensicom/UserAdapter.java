package com.example.ensicom;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {
    public static final String DATABASE_URL = "https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app/";
    List<User> userList;
    List<String> userUidList;
    public UserAdapter(List<User> userList, List<String> userUidList) {
        this.userList = userList;
        this.userUidList = userUidList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_user_item, parent, false);
        return new UserHolder(view);
    }

    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        int currentPosition = position;
        User user = userList.get(position);
        String userUID = userUidList.get(position);
        String profilePictureUrl = user.getProfilePicture();
        String username = user.getUsername();
        Boolean isAdmin = user.isAdmin();

        if (!profilePictureUrl.equals("")) {
            Glide.with(holder.getUserProfilePicture().getContext())
                    .load(user.getProfilePicture())
                    .circleCrop()
                    .into(holder.getUserProfilePicture());
        }
        holder.getUserProfilePicture().setOnClickListener(v -> {
            Intent intent = new Intent(holder.getUserName().getContext(), ProfileActivity.class);
            intent.putExtra("userId", userUID);
            holder.getUserName().getContext().startActivity(intent);
        });
        holder.getUserName().setText(username);
        holder.getUserName().setOnClickListener( v -> {
            Intent intent = new Intent(holder.getUserName().getContext(), ProfileActivity.class);
            intent.putExtra("userId", userUID);
            holder.getUserName().getContext().startActivity(intent);
        });
        if (isAdmin) {
            holder.getUserStatus().setText("Admin");
        }
        if (!isAdmin) {
            holder.getUserStatus().setText("Utilisateur");
            holder.getPutAdmin().setVisibility(View.VISIBLE);
        }
        holder.getPutAdmin().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.getPutAdmin().getContext());
            builder.setTitle("Promouvoir en admin");
            builder.setMessage("Voulez-vous promouvoir " + username + " en admin ?");
            builder.setPositiveButton("Oui", (dialog, which) -> {
                DatabaseReference userRef = FirebaseDatabase
                        .getInstance(DATABASE_URL)
                        .getReference("user")
                        .child(userUID);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                            if (isAdmin) {
                                Toast.makeText(holder.getPutAdmin().getContext(), "Cet utilisateur est déjà dieu", Toast.LENGTH_SHORT).show();
                            } else {
                                userRef.child("admin").setValue(true);
                                notifyItemRangeChanged(currentPosition, userList.size());
                                Toast.makeText(holder.getPutAdmin().getContext(), "Promu en admin", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            });
            builder.setNegativeButton("Non", (dialog, which) -> {
                Toast.makeText(holder.getPutAdmin().getContext(), "Annulé", Toast.LENGTH_SHORT).show();
            });
            builder.show();
        });
        holder.getDeleteAdmin().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.getDeleteAdmin().getContext());
            builder.setTitle("Rétrograder en utilisateur");
            builder.setMessage("Voulez-vous rétrograder " + username + " en utilisateur ?");
            builder.setPositiveButton("Oui", (dialog, which) -> {
                DatabaseReference userRef = FirebaseDatabase
                        .getInstance(DATABASE_URL)
                        .getReference("user")
                        .child(userUID);
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                            if (!isAdmin) {
                                Toast.makeText(holder.getPutAdmin().getContext(), "Cet utilisateur est déjà déchu", Toast.LENGTH_SHORT).show();
                            } else {
                                userRef.child("admin").setValue(false);
                                notifyItemRangeChanged(currentPosition, userList.size());
                                Toast.makeText(holder.getPutAdmin().getContext(), "Rétrogradé en utilisateur", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            });
            builder.setNegativeButton("Non", (dialog, which) -> {
                Toast.makeText(holder.getDeleteAdmin().getContext(), "Annulé", Toast.LENGTH_SHORT).show();
            });
            builder.show();
        });
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
}

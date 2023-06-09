package com.example.ensicom;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail;
    EditText loginPassword;
    FirebaseAuth mAuth;
    Button mLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        }
        loginEmail=findViewById(R.id.editTextEmailLogin);
        loginPassword=findViewById(R.id.editTextPasswordLogin);
        mLogin = findViewById(R.id.buttonLogin);
        mAuth = FirebaseAuth.getInstance();
        mLogin.setOnClickListener(view -> {
            String email= loginEmail.getText().toString();
            String password= loginPassword.getText().toString();
            if (email.isEmpty() && password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Veuillez entrer un email et un mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()){
                Toast.makeText(LoginActivity.this, "Veuillez entrer un email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Veuillez entrer un mot de passe", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.signInWithEmailAndPassword(email, password);
            loginUser(email, password);
        });
        Button loginToRegister = findViewById(R.id.buttonLoginToRegister);
        loginToRegister.setOnClickListener(view -> {
            Intent mainIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });

    }
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Connecté", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainIntent);
            }
        }).addOnFailureListener(e -> {
            if (e.getMessage().equals("The password is invalid or the user does not have a password.")) {
                Toast.makeText(LoginActivity.this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
            if (e.getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                Toast.makeText(LoginActivity.this, "Email incorrect", Toast.LENGTH_SHORT).show();
            }
        })
        ;
    }
}
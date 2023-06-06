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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    EditText registerEmail;
    EditText registerPassword;
    FirebaseAuth mAuth;
    Button mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerEmail=findViewById(R.id.editTextTextEmailAddressRegister);
        registerPassword=findViewById(R.id.editTextPasswordRegister);
        mRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                String email= registerEmail.getText().toString();
                String password= registerPassword.getText().toString();
                createAccount(email,password);
            }
        });
        Button Register_To_Login = findViewById(R.id.buttonRegisterToLogin);
        Register_To_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });
    }
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(RegistrationActivity.this, SetupProfileActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            if (password.length()<8) {
                                Toast.makeText(RegistrationActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(RegistrationActivity.this, "Email is already taken.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }
}

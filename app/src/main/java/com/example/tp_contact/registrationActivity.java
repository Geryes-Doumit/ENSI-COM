package com.example.tp_contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class registrationActivity extends AppCompatActivity {
    EditText registerEmail;
    EditText registerPassword;
    EditText registerName;
    FirebaseAuth mAuth;
    Button mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerEmail=findViewById(R.id.editTextTextEmailAddressRegister);
        registerPassword=findViewById(R.id.editTextTextPasswordRegister);
        registerName=findViewById(R.id.editText_registerName);
        mRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                String email= registerEmail.getText().toString();
                String password= registerPassword.getText().toString();
                String name= registerName.getText().toString();
                registerUser(email,password,name);
            }
        });
    }
    private void registerUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // save user info in database
                            Toast.makeText(registrationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

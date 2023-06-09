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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class    RegistrationActivity extends AppCompatActivity {
    EditText registerEmail;
    EditText registerPassword;
    FirebaseAuth mAuth;
    Button mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent mainIntent = new Intent(RegistrationActivity.this, HomeActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        }
        registerEmail=findViewById(R.id.editTextTextEmailAddressRegister);
        registerPassword=findViewById(R.id.editTextPasswordRegister);
        mRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(view -> {
            String email= registerEmail.getText().toString();
            String password= registerPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(RegistrationActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            createAccount(email,password);
        });
        Button registerToLogin = findViewById(R.id.buttonRegisterToLogin);
        registerToLogin.setOnClickListener(view -> {
            Intent mainIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
        });
    }
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase.getInstance("https://projet-fin-annee-ddbef-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(new User("",email,""));
                        Toast.makeText(RegistrationActivity.this, "Compte créé", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(RegistrationActivity.this, SetupProfileActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();

                    }
                }).addOnFailureListener(e -> {
                    if (e.getMessage().equals("The email address is badly formatted.")){
                        Toast.makeText(RegistrationActivity.this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")){
                        Toast.makeText(RegistrationActivity.this, "Veuillez entrer un mot de passe d'au moins 6 caractères", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (e.getMessage().equals("The email address is already in use by another account.")){
                        Toast.makeText(RegistrationActivity.this, "Cet email est déjà utilisé, veuillez en choisir un autre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(RegistrationActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

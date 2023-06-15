package com.example.ensicom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    EditText registerEmail;
    EditText registerPassword;
    EditText registerConfirmPassword;

    FirebaseAuth mAuth;
    Button mRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent mainIntent = new Intent(RegistrationActivity.this, MainActivity1.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        }
        registerEmail=findViewById(R.id.registerEmail);
        registerPassword=findViewById(R.id.eventTitleEditText);
        registerConfirmPassword=findViewById(R.id.confirmPassword);
        mRegister = findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(view -> {
            String email= registerEmail.getText().toString();
            String password= registerPassword.getText().toString();
            String confirmPassword= registerConfirmPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                Toast.makeText(RegistrationActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(email)) {
                Toast.makeText(RegistrationActivity.this, "Veuillez entrer une adresse e-mail valide (john.smith@uha.fr)", Toast.LENGTH_SHORT).show();
                registerEmail.setText("");
                return;
            }
            if (!password.equals(confirmPassword)){
                Toast.makeText(RegistrationActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                registerPassword.setText("");
                registerConfirmPassword.setText("");
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
                                .setValue(new User("",email,"", false));
                        Toast.makeText(RegistrationActivity.this, "Compte créé", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(RegistrationActivity.this, SetupProfileActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();

                    }
                }).addOnFailureListener(e -> {
                    if (e.getMessage().equals("The email address is badly formatted.")){
                        Toast.makeText(RegistrationActivity.this, "Veuillez entrer un email valide", Toast.LENGTH_SHORT).show();
                        registerEmail.setText("");
                        return;
                    }
                    if (e.getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]")){
                        Toast.makeText(RegistrationActivity.this, "Veuillez entrer un mot de passe d'au moins 6 caractères", Toast.LENGTH_SHORT).show();
                        registerPassword.setText("");
                        registerConfirmPassword.setText("");
                        return;
                    }
                    if (e.getMessage().equals("The email address is already in use by another account.")){
                        Toast.makeText(RegistrationActivity.this, "Cet email est déjà utilisé, veuillez en choisir un autre", Toast.LENGTH_SHORT).show();
                        registerEmail.setText("");
                        registerPassword.setText("");
                        registerConfirmPassword.setText("");
                        return;
                    }
                    Toast.makeText(RegistrationActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public boolean isValidEmail(String email) {
        String pattern = "^[a-z]+-?[a-z]+\\.[a-z]+-?[a-z]+@uha\\.fr$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
}

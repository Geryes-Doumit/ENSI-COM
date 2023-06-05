package com.example.tp_contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class ContactActivity extends AppCompatActivity {

    private static final int PERMISSIONS_TO_CALL = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Contact contact = (Contact) getIntent().getSerializableExtra("CONTACT");
        if (contact != null) {
            EditText nom = findViewById(R.id.editTextTextPersonName);
            EditText prenom = findViewById(R.id.editTextTextPersonName2);
            nom.setText(contact.getNom());
            prenom.setText(contact.getPrenom());
            EditText numero = findViewById(R.id.editTextPhone);
            numero.setText(contact.getNumero());
        }

        ActionBar menu = getSupportActionBar();// Recupère les props du menu
        menu.setDisplayHomeAsUpEnabled(true);// Affiche le app icon
        menu.setDisplayShowTitleEnabled(false);// Affiche pas le titre
        //menu.setHomeAsUpIndicator(R.mipmap.ic_launcher);// Choix du logo*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_edit:
                EditText nom = findViewById(R.id.editTextTextPersonName);
                EditText prenom = findViewById(R.id.editTextTextPersonName2);
                EditText numero = findViewById(R.id.editTextPhone);
                Contact modifier = new Contact(nom.getText().toString(), prenom.getText().toString(), numero.getText().toString());
                int pos = getIntent().getIntExtra("POSITION", -1);
                Intent data = new Intent();
                data.putExtra("CONTACT", modifier);
                data.putExtra("POSITION", pos);
                setResult(RESULT_OK, data);
                finish();
                return true;

            case R.id.action_delete:
                int suppr = getIntent().getIntExtra("POSITION", -1);
                Intent datasuppr = new Intent();
                datasuppr.putExtra("POSITION", suppr);
                setResult(RESULT_FIRST_USER, datasuppr);
                finish();
                return true;

            case R.id.action_call:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_TO_CALL);
                } else {
                    appel();
                }
                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish() ;
                return true ;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_TO_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission(s) acceptée(s) à appel téléphonique
                    appel();
                } else {
                    // Permission(s) refusée(s) par l’utilisateur
                }
            }
        }
    }

    public void appel() {
        EditText et = findViewById(R.id.editTextPhone);// "action_call"= ID menu appel;
        String tel = et.getText().toString();
        Intent appel = new Intent(Intent.ACTION_CALL);
        appel.setData(Uri.parse("tel:" + tel));
        startActivity(appel);
    }

    public void ok(View v) {
        EditText nom = findViewById(R.id.editTextTextPersonName);
        EditText prenom = findViewById(R.id.editTextTextPersonName2);
        EditText numero = findViewById(R.id.editTextPhone);
        Contact modifier = new Contact(nom.getText().toString(), prenom.getText().toString(), numero.getText().toString());
        int pos = getIntent().getIntExtra("POSITION", -1);
        Intent data = new Intent();
        data.putExtra("CONTACT", modifier);
        data.putExtra("POSITION", pos);
        setResult(RESULT_OK, data);

        finish();
    }

    public void cancel(View v) {
        setResult(RESULT_CANCELED);

        finish();
    }
}
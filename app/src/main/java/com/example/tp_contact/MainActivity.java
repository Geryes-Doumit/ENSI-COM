package com.example.tp_contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {
    private static final int SAISIR = 100;
    private static final int CODE_AJOUTER = 200;
    private ContactAdapter adapter = new ContactAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Contact contact = adapter.get(i);
        Toast.makeText(this, contact.toString(), Toast.LENGTH_SHORT).show();
        Intent monIntent = new Intent(this, ContactActivity.class);
        monIntent.putExtra("CONTACT", contact);
        monIntent.putExtra("POSITION",i);

        startActivityForResult(monIntent,SAISIR);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView=(SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchView.OnQueryTextListener queryTextListener=new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                Intent monIntent = new Intent(this, ContactActivity.class);
                startActivityForResult(monIntent,CODE_AJOUTER);
                return true;
            case R.id.action_quit:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SAISIR) {

            if (resultCode == RESULT_OK) {

                Contact contact = (Contact) data.getSerializableExtra("CONTACT");
                int pos = (int) data.getSerializableExtra("POSITION");
                adapter.set(pos, contact);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Bonjour " + contact.getNom(), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Opération annulée",
                        Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_FIRST_USER) {
                int pos = (int) data.getSerializableExtra("POSITION");
                adapter.remove(pos);
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == CODE_AJOUTER) {

            if (resultCode == RESULT_OK) {

                Contact contact = (Contact) data.getSerializableExtra("CONTACT");
                adapter.add(contact);
                adapter.notifyDataSetChanged();

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Opération annulée",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

}
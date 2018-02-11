package com.example.bordia98.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class notesactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesactivity);


        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("YOUR NOTES");
        setSupportActionBar(toolbar);

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String useremail = mauth.getCurrentUser().getEmail();

        TextView welcome = (TextView)findViewById(R.id.welcomenote);
        welcome.setText("Welcome , "+ " ...");



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menunotes,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){
            case R.id.logout:{
                logout();
                return true;
            }
            case R.id.addnote:{
                addnote();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addnote() {
        Intent i = new Intent(getApplicationContext(),writing.class);
        startActivity(i);
    }

    private void logout() {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.signOut();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }
}

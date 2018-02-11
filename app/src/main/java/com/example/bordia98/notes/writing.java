package com.example.bordia98.notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class writing extends AppCompatActivity {

    String title;
    EditText titlefield,mainnotefield;
    String notesdata;
    private FirebaseAuth mauth;
    private DatabaseReference notesdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_writingnotes);
        toolbar.setTitle("Complete & Save");
        setSupportActionBar(toolbar);

        mauth = FirebaseAuth.getInstance();
        notesdatabase = FirebaseDatabase.getInstance().getReference().child("notes").child(mauth.getCurrentUser().getUid());
        titlefield = (EditText)findViewById(R.id.notestitle);
        mainnotefield=(EditText)findViewById(R.id.notescontent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuwriting,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        switch (id){
            case R.id.savenote:{
                save();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void save() {
        title = titlefield.getText().toString().trim();
        notesdata= mainnotefield.getText().toString().trim();

        if(title.length()==0||notesdata.length()==0){
            if(title.length()==0){
                titlefield.setError("title can't be empty");
                titlefield.requestFocus();
                return;
            }
            else{
                mainnotefield.setError("Write something in it");
                mainnotefield.requestFocus();
                return;
            }
        }
        else{


            final DatabaseReference newnote = notesdatabase.push();

            final Map notestructure = new HashMap();
            notestructure.put("Title",title);
            notestructure.put("Data",notesdata);
            notestructure.put("Time", ServerValue.TIMESTAMP);

            Thread mainthread = new Thread(new Runnable() {
                @Override
                public void run() {

                    newnote.setValue(notestructure).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(writing.this, "Notes has been saved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(),notesactivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(writing.this , "Error in saving notes",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

            mainthread.start();

        }
    }


}

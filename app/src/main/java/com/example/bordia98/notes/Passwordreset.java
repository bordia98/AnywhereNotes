package com.example.bordia98.notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Passwordreset extends AppCompatActivity {

    EditText emailfield;
    TextView result;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordreset);

        Toolbar toolbar =(Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setTitle("Reset Password");
        setSupportActionBar(toolbar);
        result = (TextView)findViewById(R.id.result);
        emailfield = (EditText)findViewById(R.id.resetemailid);
        Button back =(Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendresetlink();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    private void sendresetlink() {
        String email = emailfield.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailfield.setError("Enter  a valid email id");
            emailfield.requestFocus();
            return;
        }
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        result.setEnabled(true);
        mauth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful()){
                              result.setText("Email has been send successfully");
                              emailfield.setText("");
                              emailfield.setEnabled(false);
                              submit.setEnabled(false);
                          }
                          else{
                              result.setText("Try again later");
                          }
                    }
                }
        );
    }
}

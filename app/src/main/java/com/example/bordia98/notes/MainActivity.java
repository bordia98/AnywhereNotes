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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;

public class MainActivity extends AppCompatActivity {

    EditText email,password,confirmpassword;
    Button signup;
    private FirebaseAuth mAuth;
    ProgressBar pgbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mysignup = (Toolbar)findViewById(R.id.my_signuptoolbar);
        setSupportActionBar(mysignup);

        email=(EditText)findViewById(R.id.emailfield);
        password=(EditText)findViewById(R.id.passwordfield);
        signup=(Button)findViewById(R.id.signup);
        mAuth = FirebaseAuth.getInstance();
        pgbar=(ProgressBar)findViewById(R.id.pgbar);
        pgbar.setVisibility(View.GONE);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signtheuser();
            }
        });
        confirmpassword=(EditText)findViewById(R.id.confirmpasswordfield);


    }

    private void signtheuser() {
        String username = email.getText().toString().trim();
        String passwd = password.getText().toString().trim();
        String cnfpswd = confirmpassword.getText().toString().trim();
        if(username.isEmpty()){
            email.setError("email id is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            email.setError("Enter  a valid email id");
            email.requestFocus();
            return;
        }

        if(passwd.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(passwd.length()<6){
            password.setError("Minimum length of password is 6 characters");
            password.requestFocus();
            return;
        }

        if(cnfpswd.equals(passwd)){

            pgbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(username,passwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pgbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),notesactivity.class);
                        startActivity(i);
                    }
                    else{
                        if (task.getException() instanceof FirebaseAuthEmailException){
                            Toast.makeText(getApplicationContext(),"User already registered",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"make sure your email is correct and is not registered already",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

            });
        }
        else{
            confirmpassword.setError("password doesn't matched");
            confirmpassword.requestFocus();
            return;
        }

    }
}

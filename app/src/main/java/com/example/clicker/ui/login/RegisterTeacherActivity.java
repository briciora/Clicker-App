package com.example.clicker.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clicker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegisterTeacherActivity extends AppCompatActivity implements View.OnClickListener{
    EditText emailId, password;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        emailId = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        findViewById(R.id.Register).setOnClickListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }
    public void registerStudent() {
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        if (email.isEmpty()) {
            emailId.setError("Please enter an email");
            emailId.requestFocus();
        } else if (pwd.isEmpty()) {
            password.setError("Please enter a password");
            password.requestFocus();
        } else if (email.isEmpty() && pwd.isEmpty()) {
            Toast.makeText(RegisterTeacherActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
        } else if (!(email.isEmpty() && pwd.isEmpty())) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterTeacherActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterTeacherActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(RegisterTeacherActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Register:
                registerStudent();
                break;
        }
    }
}

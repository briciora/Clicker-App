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

public class RegisterStudentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText firstName, lastName, studentID, emailId, password, confirmPassword;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        studentID = findViewById(R.id.StudentID);
        emailId = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        findViewById(R.id.Register).setOnClickListener(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    public void registerStudent() {
        String firName = firstName.getText().toString();
        String lasName = lastName.getText().toString();
        String studID = studentID.getText().toString();
        String email = emailId.getText().toString();
        String pwd = password.getText().toString();
        String confPass = confirmPassword.getText().toString();
        if (firName.isEmpty()){
            firstName.setError("Please enter your first name");
            firstName.requestFocus();
        }
        else if (lasName.isEmpty()){
            lastName.setError("Please enter your last name");
            lastName.requestFocus();
        }
        else if (studID.isEmpty()){
            studentID.setError("Please enter your student ID");
            studentID.requestFocus();
        }
       else if (email.isEmpty()) {
            emailId.setError("Please enter an email");
            emailId.requestFocus();
        }
        else if (pwd.isEmpty()) {
            password.setError("Please enter a password");
            password.requestFocus();
        }
        else if (confPass.isEmpty()) {
            confirmPassword.setError("Please confirm your password");
            confirmPassword.requestFocus();
        }
        else if (email.isEmpty() && pwd.isEmpty()) {
            Toast.makeText(RegisterStudentActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
        }
        else if (!(firName.isEmpty() && lasName.isEmpty() && studID.isEmpty() && email.isEmpty() && pwd.isEmpty() && confPass.isEmpty())) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterStudentActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterStudentActivity.this, LoginActivity.class);
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
        }
        else {
            Toast.makeText(RegisterStudentActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
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


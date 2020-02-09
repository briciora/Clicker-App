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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterTeacherActivity extends AppCompatActivity implements View.OnClickListener{
    EditText firstName, lastName, emailId, courseID, password, confirmPassword;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference myRef;
    Teachers teachers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        courseID = findViewById(R.id.courseID);
        emailId = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        findViewById(R.id.Register).setOnClickListener(this);
        teachers = new Teachers();
        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Teachers");
    }
    public void registerTeacher() {
        final String firName = firstName.getText().toString();
        final String lasName = lastName.getText().toString();
        final String cID = courseID.getText().toString();
        final String email = emailId.getText().toString();
        final String pwd = password.getText().toString();
        final String confPass = confirmPassword.getText().toString();
        if (firName.isEmpty()){
            firstName.setError("Please enter your first name");
            firstName.requestFocus();
        }
        else if (lasName.isEmpty()){
            lastName.setError("Please enter your last name");
            lastName.requestFocus();
        }
        else if (cID.isEmpty()){
            lastName.setError("Please enter your course ID");
            lastName.requestFocus();
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
        else if (!(pwd.equals(confPass))) {
            Toast.makeText(RegisterTeacherActivity.this, "Passwords Are Not The Same", Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty() && pwd.isEmpty()) {
            Toast.makeText(RegisterTeacherActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
        }
        else if (!(firName.isEmpty() && lasName.isEmpty() && email.isEmpty() && pwd.isEmpty() && confPass.isEmpty())) {
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterTeacherActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        teachers.setFirstName(firName);
                        teachers.setLastName(lasName);
                        teachers.setCourseID(cID);
                        teachers.setEmail(email);
                        teachers.setPassword(pwd);
                        teachers.setConfirmPassword(confPass);
                        myRef.child(cID).setValue(teachers);
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
        }
        else {
            Toast.makeText(RegisterTeacherActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Register:
                registerTeacher();
                break;
        }
    }
}

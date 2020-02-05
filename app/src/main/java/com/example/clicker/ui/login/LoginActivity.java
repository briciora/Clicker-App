//test comment

package com.example.clicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.clicker.R;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailId, password;
    FirebaseAuth mFirebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.email);
        password = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.registerAsStudent).setOnClickListener(this);
        findViewById(R.id.registerAsTeacher).setOnClickListener(this);
        //System.out.println("AAAAAAAAAAAAAAAA");

    }
    public void userLogin(){
       String email = emailId.getText().toString();
       String pwd = password.getText().toString();
       if(email.isEmpty()){
           emailId.setError("Please enter your email");
           emailId.requestFocus();
       }
       else if(pwd.isEmpty()){
           password.setError("Please enter your password");
           password.requestFocus();
       }
       else if(email.isEmpty() && pwd.isEmpty()){
           Toast.makeText(LoginActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
       }
       else if(!(email.isEmpty() && pwd.isEmpty())){
           mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       //PUT NEW ACTIVITY HERE
                       Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       Toast.makeText(LoginActivity.this, "Login Not Successful", Toast.LENGTH_SHORT).show();
                       Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }
       else{
           Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
       }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Students").child("email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(LoginActivity.this, "STUDENT CLASS", Toast.LENGTH_SHORT).show();
                            // use "username" already exists
                        } else {
                            // "username" does not exist yet.
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                userLogin();
                break;
            case R.id.registerAsStudent:
                startActivity(new Intent(this, RegisterStudentActivity.class));
                break;
            case R.id.registerAsTeacher:
                startActivity(new Intent(this, RegisterTeacherActivity.class));
                break;
        }
    }
}

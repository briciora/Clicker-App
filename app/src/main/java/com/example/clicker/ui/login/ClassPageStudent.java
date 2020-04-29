package com.example.clicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clicker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClassPageStudent extends AppCompatActivity implements View.OnClickListener {
    private String username;
    private String course_name;
    private String course_hash; //wd
    private boolean is_teacher;
    private DatabaseReference my_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_student);

        username = getIntent().getExtras().getString("username");
        course_name = getIntent().getExtras().getString("course_name");
        course_hash = getIntent().getExtras().getString("course_hash");
        is_teacher = getIntent().getExtras().getBoolean("is_teacher");

        my_ref = FirebaseDatabase.getInstance().getReference();


        findViewById(R.id.joinSession).setOnClickListener(this);

        //populate class text view: wd
        TextView classTextView = (TextView)findViewById(R.id.className);
        classTextView.setText(course_name);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.joinSession) {
            my_ref.child("Sessions").child(course_hash).child("active").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean is_active_session = (boolean) dataSnapshot.getValue(boolean.class);

                    if (is_active_session) {
                        Intent intent = new Intent(ClassPageStudent.this, StudentSession.class);
                        intent.putExtra("is_teacher", is_teacher);
                        intent.putExtra("username", username);
                        intent.putExtra("course_name", course_name);
                        intent.putExtra("course_hash", course_hash);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(ClassPageStudent.this, "This session is not currently active", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

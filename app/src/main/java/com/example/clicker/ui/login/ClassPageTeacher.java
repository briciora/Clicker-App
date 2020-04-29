package com.example.clicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clicker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClassPageTeacher extends AppCompatActivity implements View.OnClickListener {
    private String username;
    private String course_name;
    private String course_hash; //wd
    private boolean is_teacher;
    private DatabaseReference my_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_teacher);

        username = getIntent().getExtras().getString("username");
        course_name = getIntent().getExtras().getString("course_name");
        course_hash = getIntent().getExtras().getString("course_hash"); //wd
        is_teacher = getIntent().getExtras().getBoolean("is_teacher");
        my_ref = FirebaseDatabase.getInstance().getReference();


        findViewById(R.id.startSession).setOnClickListener(this);
        findViewById(R.id.addQuestion).setOnClickListener(this);
        findViewById(R.id.go_back).setOnClickListener(this);

        //populate class text view: wd
        TextView classTextView = (TextView)findViewById(R.id.className);
        classTextView.setText(course_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startSession: {
                my_ref.child("Sessions").child(course_hash).child("active").setValue(true);
                Toast.makeText(ClassPageTeacher.this, "Session started", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, TeacherSession.class);
                intent.putExtra("is_teacher", is_teacher);
                intent.putExtra("username", username);
                intent.putExtra("course_name", course_name);
                intent.putExtra("course_hash", course_hash);
                startActivity(intent);
                break;
            }
            case R.id.addQuestion: {
                Intent intent = new Intent(this, CreateQuestion.class);
                intent.putExtra("is_teacher", is_teacher);
                intent.putExtra("username", username);
                intent.putExtra("course_name", course_name);
                intent.putExtra("course_hash", course_hash);
                startActivity(intent);
                break;
            }
            case R.id.go_back: {
                Intent intent = new Intent(this, ClassListActivity.class);
                intent.putExtra("is_teacher", is_teacher);
                intent.putExtra("username", username);
                intent.putExtra("course_name", course_name);
                intent.putExtra("course_hash", course_hash);
                startActivity(intent);
                break;
            }
        }
    }
}

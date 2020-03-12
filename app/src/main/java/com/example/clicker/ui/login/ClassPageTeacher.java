package com.example.clicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.clicker.R;

public class ClassPageTeacher extends AppCompatActivity implements View.OnClickListener {

    //  TextView className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_teacher);
        //     className =
        findViewById(R.id.startSession).setOnClickListener(this);
        findViewById(R.id.addQuestion).setOnClickListener(this);
        findViewById(R.id.roster).setOnClickListener(this);
        // findViewById(R.id.questionList).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startSession:
                //      startActivity(new Intent(this, InSessionTeacher.class));
                break;
            case R.id.addQuestion:
                startActivity(new Intent(this, CreateQuestion.class));
                break;
            case R.id.roster:
                //      startActivity(new Intent(this, ClassRoster.class));
                break;
        }
    }
}

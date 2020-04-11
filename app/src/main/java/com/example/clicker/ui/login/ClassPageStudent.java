package com.example.clicker.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.clicker.R;

public class ClassPageStudent extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_page_student);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.joinSession)
        {
            startActivity(new Intent(this, StudentSession.class));
        }
    }
}

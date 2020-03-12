package com.example.clicker.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clicker.R;

public class CreateQuestion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        Spinner questionType = findViewById(R.id.spinner_QT);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.question_type, android.R.layout.activity_list_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionType.setAdapter(adapter);
        questionType.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

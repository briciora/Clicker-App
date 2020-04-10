package com.example.clicker.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clicker.R;

public class CreateQuestion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    EditText newQuestion, choice1, choice2, choice3, choice4;
    int correctAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        newQuestion = findViewById(R.id.newQuestion);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);

        Spinner spinner = (Spinner) findViewById(R.id.correctAnswer);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.correctAnswers, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submitQuestion();
                break;
        }
    }

    public void submitQuestion(){
        //send question data to firebase and populate question list
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        if(pos == 0){
            correctAnswer = 1;
        }
        else if(pos == 1){
            correctAnswer = 2;
        }
        else if(pos == 2){
            correctAnswer = 3;
        }
        else if(pos == 3){
            correctAnswer = 4;
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

package com.example.clicker.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.clicker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

enum QuestionType {
    MULTIPLE_CHOICE,
    SHORT_ANSWER
}

public class CreateQuestion extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference my_ref;
    private String username;
    private String course_name;
    private String course_hash;
    private String question_text;
    private QuestionType question_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        my_ref = FirebaseDatabase.getInstance().getReference();
        username = getIntent().getExtras().getString("username");
        course_name = getIntent().getExtras().getString("course_name");
        course_hash = getIntent().getExtras().getString("course_hash");
        question_type = QuestionType.SHORT_ANSWER;

        Spinner questionType = (Spinner)findViewById(R.id.spinner_QT);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this, R.array.question_type, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionType.setAdapter(spinner_adapter);
        questionType.setOnItemSelectedListener(this);

        final Button button = findViewById(R.id.create_button);
        button.setOnClickListener(this);

        final FloatingActionButton fab = findViewById(R.id.addChoice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        EditText question_edit_text = (EditText) findViewById(R.id.newQuestion);
        question_text = sanitizeQuestionString(question_edit_text.getText().toString()).getKey();

        Map<String, String> answer_choice_map = new HashMap<String, String>();
        String choice1 = ((EditText)findViewById(R.id.answer1)).getText().toString();
        Pair<String, Boolean> p1 = sanitizeQuestionString(choice1);

        final Question q = new Question();
        q.setQuestionText(question_text);

        switch(question_type) {
            case MULTIPLE_CHOICE: {
                String choice2 = ((EditText)findViewById(R.id.answer2)).getText().toString();
                String choice3 = ((EditText)findViewById(R.id.answer3)).getText().toString();
                String choice4 = ((EditText)findViewById(R.id.answer4)).getText().toString();

//                Pair<String, Boolean> p2 = sanitizeQuestionString(choice2);
//                Pair<String, Boolean> p3 = sanitizeQuestionString(choice3);
//                Pair<String, Boolean> p4 = sanitizeQuestionString(choice4);

//                answer_choice_map.put(p1.getKey(), p1.getValue() ? "true" : "false");
//                answer_choice_map.put(p2.getKey(), p2.getValue() ? "true" : "false");
//                answer_choice_map.put(p3.getKey(), p3.getValue() ? "true" : "false");
//                answer_choice_map.put(p4.getKey(), p4.getValue() ? "true" : "false");
                answer_choice_map.put(choice1, "false");
                answer_choice_map.put(choice2, "false");
                answer_choice_map.put(choice3, "false");
                answer_choice_map.put(choice4, "false");

                break;
            }
            case SHORT_ANSWER: {
//                answer_choice_map.put(p1.getKey(), p1.getValue() ? "true" : "false");
                answer_choice_map.put(choice1, "false");
                break;
            }
        }

        q.setAnswerCorrectnessMap(answer_choice_map);
        q.sanitize(true);

        my_ref.child("Sessions").child(course_hash).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                my_ref.child("Sessions").child(course_hash).child("Questions").child(q.getQuestionText()).setValue(q);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public Pair<String, Boolean> sanitizeQuestionString(String str) {
        boolean is_correct = str.contains("\\correct");

        String sanitized = str.replace("\\correct", "");
        int i = 0;
        int j = sanitized.length() - 1;
        while (sanitized.charAt(i) == ' ') { ++i; }
        while (sanitized.charAt(j) == ' ') { --j; }
        sanitized = str.substring(i, j + 1);

        List<Pair<String, String>> contained_list = new ArrayList<Pair<String, String>>(
                Arrays.asList(
                        Pair.of(".", "\\period"),
                        Pair.of("$", "\\dollarSign"),
                        Pair.of("[", "\\leftSquareBracket"),
                        Pair.of("]", "\\rightSquareBracket"),
                        Pair.of("#", "\\hashtag"),
                        Pair.of("/", "\\forwardSlash")
                )
        );

        for (int count = 0; count < contained_list.size(); ++count) {
            Pair<String, String> p = contained_list.get(count);
            sanitized = sanitized.replace(p.getKey(), p.getValue());
        }

        return Pair.of(sanitized, is_correct);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

        View fab = findViewById(R.id.addChoice);
        EditText et = findViewById(R.id.answer1);
        if (text.equals("Multiple Choice")) {
            et.setHint("Type the answer text for choice A.");
            fab.setVisibility(View.VISIBLE);
            findViewById(R.id.answer2).setVisibility(View.VISIBLE);
            findViewById(R.id.answer3).setVisibility(View.VISIBLE);
            findViewById(R.id.answer4).setVisibility(View.VISIBLE);
            question_type = QuestionType.MULTIPLE_CHOICE;
        }
        else if (text.equals("Short Answer")) {
            et.setHint("Type the answer to your question here.");
            fab.setVisibility(View.GONE);
            findViewById(R.id.answer2).setVisibility(View.GONE);
            findViewById(R.id.answer3).setVisibility(View.GONE);
            findViewById(R.id.answer4).setVisibility(View.GONE);
            question_type = QuestionType.SHORT_ANSWER;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

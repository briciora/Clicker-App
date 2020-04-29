package com.example.clicker.ui.login;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clicker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentSession extends AppCompatActivity implements View.OnClickListener {
    private String username;
    private String course_name;
    private String course_hash; //wd
    private boolean is_teacher;
    private DatabaseReference my_ref;
    private Question current_q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_session);

        username = getIntent().getExtras().getString("username");
        course_name = getIntent().getExtras().getString("course_name");
        course_hash = getIntent().getExtras().getString("course_hash");
        is_teacher = getIntent().getExtras().getBoolean("is_teacher");

        my_ref = FirebaseDatabase.getInstance().getReference();

        //populate class text view: wd
        TextView classTextView = (TextView)findViewById(R.id.title);
        classTextView.setText(course_name);

        Button btn = (Button) findViewById(R.id.exit);
        btn.setOnClickListener(this);

        my_ref.child("Sessions").child(course_hash).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                    Question q = (Question) snapshotChild.getValue(Question.class);
                    if (q.isActive()) {
                        q.sanitize(false);
                        current_q = new Question(q);
                        TextView tv = (TextView) findViewById(R.id.question);
                        tv.setText(q.getQuestionText());

                        Map<String, String> answer_choice_map = q.getAnswerCorrectnessMap();
                        List<RadioButton> rb_list = new ArrayList<RadioButton>(
                                Arrays.asList(
                                        (RadioButton) findViewById(R.id.answerA),
                                        (RadioButton) findViewById(R.id.answerB),
                                        (RadioButton) findViewById(R.id.answerC),
                                        (RadioButton) findViewById(R.id.answerD)
                                )
                        );

                        EditText et = (EditText) findViewById(R.id.shortQuestionAnswer);

                        if (answer_choice_map.size() > 1) {
                            et.setVisibility(View.INVISIBLE);

                            int counter = 0;
                            for (Map.Entry<String, String> e : q.getAnswerCorrectnessMap().entrySet()) {
                                rb_list.get(counter).setVisibility(View.VISIBLE);
                                rb_list.get(counter).setText(e.getKey());
                                ++counter;
                            }
                        }
                        else if (answer_choice_map.size() == 1) {
                            et.setVisibility(View.VISIBLE);
                            findViewById(R.id.radioGroup).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);

        if (rg.getVisibility() == View.VISIBLE) {
            int selectedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton checked_rb = (RadioButton) findViewById(selectedId);
                String text = checked_rb.getText().toString();
                String question_text = current_q.getQuestionText();
                my_ref.child("Sessions")
                        .child(course_hash)
                        .child("Responses")
                        .child(username)
                        .child(question_text)
                        .setValue(text);
            } else {
                Toast.makeText(StudentSession.this, "No answer selected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

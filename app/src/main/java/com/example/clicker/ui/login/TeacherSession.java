package com.example.clicker.ui.login;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clicker.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TeacherSession extends AppCompatActivity implements View.OnClickListener {
    private Boolean is_teacher;
    private String username;
    private String course_hash;
    private String course_name;
    private DatabaseReference my_ref;
    private CustomListAdapter<Question> la;
    private int question_chars_displayed;
    private List<Question> my_questions;
    private int mSelectedItem;
    private Boolean wait_for_worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_session);

        Button btn = (Button) findViewById(R.id.end);
        btn.setOnClickListener(this);

        mSelectedItem = -1;
        is_teacher = getIntent().getExtras().getBoolean("is_teacher");
        username = getIntent().getExtras().getString("username");
        course_hash = getIntent().getExtras().getString("course_hash");
        course_name = getIntent().getExtras().getString("course_name");
        my_ref = FirebaseDatabase.getInstance().getReference();
        question_chars_displayed = 45;
        my_questions = new ArrayList<Question>();
        wait_for_worker = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        populateList();
    }

    private void populateList() {
        final ListView lv = (ListView) findViewById(R.id.listOfQuestions);

        my_ref.child("Sessions").child(course_hash).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Question> session_questions = new ArrayList<Question>();
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    Question child_q = new Question((Question) child.getValue(Question.class));
                    session_questions.add(child_q);
                }

                my_questions.clear();
                for (Question question : session_questions) {
                    if (question.isActive()) {
                        mSelectedItem = session_questions.indexOf(question);
                    }
                    my_questions.add(question);
                }

                la = new CustomListAdapter<Question>(TeacherSession.this, R.layout.listview_item_class, session_questions) {
                    @Override
                    public View getView(int position, View convert_view, ViewGroup parent) {
                        boolean wait = wait_for_worker;
                        while (wait) {
                            wait = wait_for_worker;
                        }

                        View view = initView(convert_view);

                        Question q = new Question(getItem(position));
                        q.sanitize(false);

                        if (q != null) {
                            TextView tv1 = (TextView) view.findViewById(R.id.Item1);
                            TextView tv2 = (TextView) view.findViewById(R.id.Item2);
                            if (tv1 != null) {
                                String question_text = q.getQuestionText();
                                if (question_text.length() > question_chars_displayed) {
                                    question_text = question_text.substring(0, question_chars_displayed) + "...";
                                }
                                tv1.setText(question_text);
                            }
                            if (tv2 != null) {
                                tv2.setText(q.getAnswerCorrectnessMap().size() == 1 ? "Short answer" : "Multiple Choice");
                            }
                        }

                        RelativeLayout my_rl = (RelativeLayout) view.findViewById(R.id.list_item_rel_layout);
                        if (mSelectedItem == position) {
                            my_rl.setBackgroundColor(getResources().getColor(R.color.selected));
                        }
                        else {
                            my_rl.setBackgroundColor(getResources().getColor(R.color.white));
                        }

                        return view;
                    }
                };
                lv.setAdapter(la);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ghettoSemaphore();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ghettoSemaphore();

                if (mSelectedItem == position) {
                    mSelectedItem = -1;
                    wait_for_worker = true;
                    my_ref.child("Sessions").child(course_hash).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            List<Question> db_questions = new ArrayList<Question>();
                            for (DataSnapshot snapshot_child : dataSnapshot.getChildren()) {
                                Question question = (Question) snapshot_child.getValue(Question.class);
                                if (question.isActive()) {
                                    mSelectedItem = -1;
                                    wait_for_worker = true;
                                    my_ref.child("Sessions")
                                            .child(course_hash)
                                            .child("Questions")
                                            .child(question.getQuestionText())
                                            .child("isActive")
                                            .setValue(false);
                                    wait_for_worker = false;
                                    break;
                                }
                            }


                            la.notifyDataSetChanged();
                            view.setSelected(true);

                            populateList();
                            wait_for_worker = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    mSelectedItem = position;
                    wait_for_worker = true;

                    my_ref.child("Sessions").child(course_hash).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String question_text = my_questions.get(mSelectedItem).getQuestionText();
                            List<Question> db_questions = new ArrayList<Question>();
                            for (DataSnapshot snapshot_child : dataSnapshot.getChildren()) {
                                Question question = (Question) snapshot_child.getValue(Question.class);
                                if (question.isActive()) {
                                    mSelectedItem = -1;
                                    wait_for_worker = true;
                                    my_ref.child("Sessions")
                                            .child(course_hash)
                                            .child("Questions")
                                            .child(question.getQuestionText())
                                            .child("isActive")
                                            .setValue(false);
                                    wait_for_worker = false;
                                    break;
                                }
                            }

                            wait_for_worker = true;
                            my_ref
                                    .child("Sessions").child(course_hash)
                                    .child("Questions")
                                    .child(question_text)
                                    .child("isActive")
                                    .setValue(true);
                            wait_for_worker = false;

                            la.notifyDataSetChanged();
                            view.setSelected(true);


                            populateList();
                            wait_for_worker = false;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void ghettoSemaphore() {
        boolean wait = wait_for_worker;
        while (wait) {
            wait = wait_for_worker;
        }
    }

    @Override
    public void onClick(View v) {
        my_ref.child("Sessions").child(course_hash).child("active").setValue(false);
        Toast.makeText(TeacherSession.this, "Session ended", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(TeacherSession.this, ClassPageTeacher.class);
        intent.putExtra("is_teacher", (boolean)is_teacher);
        intent.putExtra("username", username);
        intent.putExtra("course_hash", course_hash);
        intent.putExtra("course_name", course_name);
        startActivity(intent);
    }
}

package com.example.clicker.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.clicker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassListActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private boolean is_teacher;
    private String username;
    private String teacher_name;
    private Course course_to_add;
    private String course_hash;
    private ListView list_view;
    private ArrayList<String> my_course_ids;
    private CustomListAdapter list_adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_list);
        final FloatingActionButton addClassButton = findViewById(R.id.addClass);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addClass(v);
            }
        });

        // initialize member variables
        myRef = FirebaseDatabase.getInstance().getReference();
        is_teacher = getIntent().getExtras().getBoolean("is_teacher");
        username = getIntent().getExtras().getString("username");
        teacher_name = "";
        my_course_ids = new ArrayList<String>();
    }

    @Override
    // called whenever this activity is given the focus
    protected void onStart() {
        super.onStart();
        populateList();
    }

    @Override
    // called whenever this activity loses the focus
    protected void onStop() {
        super.onStop();
    }

    // populate list view with classes from database
    private void populateList() {
        // grab a reference to the listview element in activity_class_list.xml
        list_view = (ListView) findViewById(R.id.listOfClasses);

        // get string representing which node we want to query in database
        String db_parent_node = is_teacher ? "Teachers" : "Students";

        // query database for current user's node
        myRef.child(db_parent_node).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // marshal datasnapshot into student or teacher object depending on user identity
                // (this must be declared final so that it can be accessed in following async courses event listener)
                final User me = is_teacher ? (Teacher) dataSnapshot.getValue(Teacher.class) : (Student) dataSnapshot.getValue(Student.class);

                // add all course hash ids to list
                for (Map.Entry<String, String> map_entry : me.getMap().entrySet()) {
                    my_course_ids.add(map_entry.getValue());
                }

                // query databases for all courses
                myRef.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // make new list and add to it all the courses that the current user is associated with
                        List<Course> my_courses = new ArrayList<Course>();
                        for (DataSnapshot snapshotChild : dataSnapshot.getChildren()) {
                            Course c = (Course) snapshotChild.getValue(Course.class);
                            if (me.getMap().containsKey(Long.toString(c.generateCourseHash()))) {
                                my_courses.add((Course) snapshotChild.getValue(Course.class));
                            }
                        }

                        // set list adapter on listview
                        list_adapter = new CustomListAdapter(ClassListActivity.this, R.layout.listview_item_class, my_courses);
                        list_view.setAdapter(list_adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Read failed. Error code: " + databaseError.getCode());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Read failed. Error code: " + databaseError.getCode());
            }
        });

        //when a a class in the list is clicked, launch the next activity
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(is_teacher)
                {
                    //get class name and put in intent

                    Intent teacher_class_page = new Intent(ClassListActivity.this, ClassPageTeacher.class);
                    startActivity(teacher_class_page);
                }
                else
                {
                    //get class name and put in intent
                    Intent student_class_page = new Intent(ClassListActivity.this, ClassPageStudent.class);
                    startActivity(student_class_page);
                }
            }
        });
    }

    void addClass(View view) {
        // instantiate new course to add
        course_to_add = new Course();

        // get view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(is_teacher ? R.layout.teacher_prompt : R.layout.student_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set  to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText courseID = promptsView.findViewById(R.id.etID);
        final EditText teacher = promptsView.findViewById(R.id.etTeacher);
        final EditText description = promptsView.findViewById(R.id.etName);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // pull text data from prompt text boxes
                        final String name = description.getText().toString();
                        final String identification = courseID.getText().toString();

                        // begin building course
                        course_to_add.setCourseId(identification);
                        course_to_add.setCourseName(name);

                        if (is_teacher) {
                            myRef.child("Teachers").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // marshal dataSnapshot from JSON object into java class
                                    Teacher me = (Teacher) dataSnapshot.getValue(Teacher.class);

                                    // set teacher name for course as the name bound to current user account
                                    // so as to not cause any issues (async sensitive)
                                    teacher_name = me.getFirstName() + " " + me.getLastName();
                                    course_to_add.setTeacher(teacher_name);

                                    // generate course hash (seeded from the course ID and teacher name)
                                    course_hash = Long.toString(course_to_add.generateCourseHash());

                                    // if we are a teacher, create a new course as child of the "Courses" node in db
                                    // with course hash as node identifier
                                    myRef.child("Courses").child(course_hash).setValue(course_to_add);


//                                  // now update teacher's list of course hashes for the courses they teach in db
                                    myRef.child("Teachers").child(username).child("CourseIDs").child(course_hash).setValue(course_hash);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    System.out.println("Read failed. Error code: " + databaseError.getCode());
                                }
                            });
                        } else {
                            // pull teacher name entered by student user from text box in prompt
                            // and update the course
                            teacher_name = teacher.getText().toString();
                            course_to_add.setTeacher(teacher_name);

                            // generate course hash (seeded from the course ID and teacher name)
                            course_hash = Long.toString(course_to_add.generateCourseHash());

                            // if we are a student, calculate unique hash for course
                            // so that we can check to see if class exists as a child of "Courses" node in db
                            // (i.e. check to see if class has been created by a teacher)
                            myRef.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(course_hash)) {
                                        // course is in db so it has been created by a teacher. add course hash to list
                                        // of hashes in db representing classes that current student user is enrolled in
                                        myRef.child("Students").child(username).child("CourseIDs").child(course_hash).setValue(course_hash);
                                    } else {
                                        // course is not in db yet. Notify user
                                        Toast.makeText(ClassListActivity.this, "Error: that course has not been created by a teacher yet", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    System.out.println("Read for course check failed. Error code: " + databaseError.getCode());
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

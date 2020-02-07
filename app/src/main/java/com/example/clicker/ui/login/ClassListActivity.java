package com.example.clicker.ui.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.clicker.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ClassListActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        final Button addClassButton = findViewById(R.id.addClass);
        addClassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addClass(v);
            }
        });
    }

    void addToDB()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Courses");
        myRef.setValue("Databases");
    }

    void addClass(View view)
    {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText courseName = promptsView.findViewById(R.id.etName);
        final EditText courseID = promptsView.findViewById(R.id.etID);

        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        final String name = courseName.getText().toString();
                        final String id_ = courseID.getText().toString();

                        //ADD BACKEND TO STORE IN DB
                    }
                })
                .setNegativeButton("C" +"ancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}

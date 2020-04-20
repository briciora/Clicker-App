package com.example.clicker.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.clicker.R;

import java.util.List;

// custom list adapter for populating listview with custom objects
// TODO: GENERALIZE INTO PARENT AND CHILD CLASS THAT WORK WITH LIST OF ANY OBJECT TYPE
public class CustomListAdapter extends ArrayAdapter<Course> {
    private int resource_layout;
    private Context my_context;

    // set list adapter (get context (context), xml class to modify (resource), and list to populate with(courses))
    public CustomListAdapter(Context context, int resource, List<Course> courses) {
        super(context, resource, courses);
        this.resource_layout = resource;
        this.my_context = context;
    }

    // populate custom xml element with custom object data
    @Override
    public View getView(int position, View convert_view, ViewGroup parent) {
        View view = convert_view;
        if (view == null) {
            LayoutInflater view_inflater = LayoutInflater.from(my_context);
            view = view_inflater.inflate(resource_layout, null);
        }

        Course c = getItem(position);

        if (c != null) {
            TextView tv1 = (TextView) view.findViewById(R.id.class_name);
            TextView tv2 = (TextView) view.findViewById(R.id.teacher_name);

            if (tv1 != null) {
                tv1.setText(c.getCourseName());
            }
            if (tv2 != null) {
                tv2.setText(c.getTeacher());
            }
        }
        return view;
    }
}

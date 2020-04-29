
package com.example.clicker.ui.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.clicker.R;

import java.util.List;
import java.util.function.Function;

// custom list adapter for populating listview with custom objects
public class CustomListAdapter<T> extends ArrayAdapter<T> {
    private int resource_layout;
    private Context my_context;
    private Function<CustomListAdapter<T>, View> customPopulateFunc;

    // set list adapter (get context (context), xml class to modify (resource), and list to populate with(courses))
    public CustomListAdapter(Context context, int resource, List<T> courses) {
        super(context, resource, courses);
        this.resource_layout = resource;
        this.my_context = context;
    }

    public View initView(View convert_view) {
        View v = convert_view;
        if (convert_view == null) {
            LayoutInflater view_inflater = LayoutInflater.from(my_context);
            v = view_inflater.inflate(resource_layout, null);
        }
        return v;
    }
}
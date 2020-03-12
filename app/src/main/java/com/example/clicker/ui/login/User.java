package com.example.clicker.ui.login;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public abstract class User {
    private String first_name;
    private String last_name;
    private String email_address;
    private Map<String, String> course_map;

    public User() { initMap(); }

    public String getFirstName() { return first_name; }

    public void setFirstName(String first_name) { this.first_name = first_name; }

    public String getLastName() { return last_name; }

    public void setLastName(String last_name) { this.last_name = last_name; }

    public String getEmailAddress() { return this.email_address; }

    public void setEmailAddress(String email_address) { this.email_address = email_address; }

    public void initMap() { this.course_map = new HashMap<String, String>(); }

    @PropertyName("CourseIDs")
    public Map<String, String> getMap() { return this.course_map; }

    @PropertyName("CourseIDs")
    public void setMap(Map<String, String> id_teach_map) {
        for (Map.Entry<String, String> e : id_teach_map.entrySet()) {
            this.course_map.put(e.getKey(), e.getValue());
        }
    }
}

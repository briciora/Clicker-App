package com.example.clicker.ui.login;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Session {
    private Course course;
    private Map<String, Question> question_map;
    private Map<String, Map<String, String>> response_map;
    private boolean is_active;

    Session() {
        this.question_map = new HashMap<String, Question>();
        this.response_map = new HashMap<String, Map<String, String>>();
        this.is_active = false;
    }

    Session(Course c) {
        this.question_map = new HashMap<String, Question>();
        this.response_map = new HashMap<String, Map<String, String>>();
        this.is_active = false;
        setCourse(c);
    }

    public Course getCourse() { return this.course; }

    public boolean isActive() { return this.is_active; }

    @PropertyName("Questions")
    public Map<String, Question> getQuestionMap() { return this.question_map; }

    @PropertyName("Responses")
    public Map<String, Map<String, String>> getUserMap() { return this.response_map; }

    public void setCourse(Course c) { this.course = new Course(c); }

    public void toggleActive() { this.is_active = !this.is_active; }

    @PropertyName("Questions")
    public void setQuestionMap(Map<String, Question> map_in) {
        for (Map.Entry<String, Question> e : map_in.entrySet()) {
            this.question_map.put(e.getKey(), new Question(e.getValue()));
        }
    }

    @PropertyName("Responses")
    public void setUserMap(Map<String, Map<String, String>> map_in) {
        for (Map.Entry<String, Map<String, String>> e1 : map_in.entrySet()) {
            Map<String, String> inner_map = new HashMap<String, String>();
            for (Map.Entry<String, String> e2 : e1.getValue().entrySet()) {
                inner_map.put(e2.getKey(), e2.getValue());
            }
            this.response_map.put(e1.getKey(), inner_map);
        }
    }

    public void addQuestion(Question q) {
        this.question_map.put(q.getQuestionText(), new Question(q));
    }
}

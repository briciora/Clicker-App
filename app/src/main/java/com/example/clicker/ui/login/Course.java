package com.example.clicker.ui.login;

import java.util.Date;
import java.util.Map;

public class Course {
    private String courseId;
    private String courseName;
    private String teacher;

    Course() {
        this.courseId = "";
        this.courseName = "";
        this.teacher = "";
    }

    Course(Course other) {
        this.courseId = other.courseId;
        this.courseName = other.courseName;
        this.teacher = other.teacher;
    }

    public String getCourseId() { return this.courseId; }

    public String getCourseName(){ return this.courseName; }

    public String getTeacher() { return this.teacher; }

    public void setCourseId(String courseNum){
        this.courseId = courseNum;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setTeacher(String teacher) { this.teacher = teacher; }

    public static long generateCourseHash(String courseId, String teacher) {
        String string_to_hash = courseId + teacher;
        long hash = 0;
        int p = 11;
        long m = 1000000009;
        long pow = 1;
        for (int i = 0; i < string_to_hash.length(); ++i) {
            char c = string_to_hash.charAt(i);
            hash = (hash + (c - 'a' + 1) * pow) % m;
            pow = (pow * p) % m;
        }
        return hash;
    }
}

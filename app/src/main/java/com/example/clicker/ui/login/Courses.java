package com.example.clicker.ui.login;

import java.util.Date;

public class Courses {
    private int courseNum;
    private String courseName;
    private Date courseTime;

    public int getCourseNum() {return courseNum;}
    public String getCourseName(){return courseName;}
    public Date getCourseTime(){return courseTime;}

    public void setCourseNum(int courseNum){
        this.courseNum = courseNum;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    public void setCourseTime(Date courseTime){
        this.courseTime = courseTime;
    }

}

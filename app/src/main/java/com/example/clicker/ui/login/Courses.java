package com.example.clicker.ui.login;

import java.util.Date;

public class Courses {
    private String courseNum;
    private String courseName;
    //private Date courseTime;

    public String getCourseNum() {return courseNum;}
    public String getCourseName(){return courseName;}
    //public Date getCourseTime(){return courseTime;}

    public void setCourseNum(String courseNum){
        this.courseNum = courseNum;
    }

    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    /*
    public void setCourseTime(Date courseTime){
        this.courseTime = courseTime;
    }
    */
}

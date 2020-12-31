package com.eduzap.android.ui.drawer.home.Model;

import androidx.annotation.Keep;

import java.util.ArrayList;

@Keep
public class CoursesModel {
    private String CourseTitle;
    private ArrayList<SubjectsModel> SubjectItem;

    public CoursesModel() {
    }

    public CoursesModel(String courseTitle, ArrayList<SubjectsModel> subjectItem) {
        CourseTitle = courseTitle;
        SubjectItem = subjectItem;
    }

    public String getCourseTitle() {
        return CourseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        CourseTitle = courseTitle;
    }

    public ArrayList<SubjectsModel> getSubjectItem() {
        return SubjectItem;
    }

    public void setSubjectItem(ArrayList<SubjectsModel> subjectItem) {
        SubjectItem = subjectItem;
    }
}

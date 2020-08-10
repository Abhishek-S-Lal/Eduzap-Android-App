package com.eduzap.android.ui.drawer.subject_details;

public class SubjectDetailsModel {
    private String subjectImage, subjectName, subjectDescription, teacherName;

    public SubjectDetailsModel() {
    }

    public SubjectDetailsModel(String subjectImage, String subjectName, String subjectDescription, String teacherName) {
        this.subjectImage = subjectImage;
        this.subjectName = subjectName;
        this.subjectDescription = subjectDescription;
        this.teacherName = teacherName;
    }

    public String getSubjectImage() {
        return subjectImage;
    }

    public void setSubjectImage(String subjectImage) {
        this.subjectImage = subjectImage;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

}

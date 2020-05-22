package com.eduzap.android.ui.home.Interface;

import com.eduzap.android.ui.home.Model.CoursesModel;

import java.util.List;

public interface IFirebaseLoadListener {
    void onFirebaseLoadSuccess(List<CoursesModel> coursesModelList);

    void FirebaseLoadFailed(String message);
}


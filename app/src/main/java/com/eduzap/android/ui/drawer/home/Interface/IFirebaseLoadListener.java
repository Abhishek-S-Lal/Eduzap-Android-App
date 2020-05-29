package com.eduzap.android.ui.drawer.home.Interface;

import com.eduzap.android.ui.drawer.home.Model.CoursesModel;

import java.util.List;

public interface IFirebaseLoadListener {
    void onFirebaseLoadSuccess(List<CoursesModel> coursesModelList);

    void FirebaseLoadFailed(String message);
}


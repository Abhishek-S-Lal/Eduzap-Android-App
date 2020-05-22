package com.eduzap.android.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.HomeActivity;
import com.eduzap.android.MainActivity;
import com.eduzap.android.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    RecyclerView mCoursesRecyclerView;
    CoursesAdapter mCoursesAdapter;

//    RecyclerView mSubjectsRecyclerView;
//    SubjectsAdapter mSubjectsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mCoursesRecyclerView = root.findViewById(R.id.coursesRecyclerView);
        mCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity())); //this will create recycler view in linear layout.

        mCoursesAdapter = new CoursesAdapter(this, getCoursesList());
        mCoursesRecyclerView.setAdapter(mCoursesAdapter);

//        mSubjectsRecyclerView = root.findViewById(R.id.subjectsRecyclerView);
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        mSubjectsRecyclerView.setLayoutManager(horizontalLayoutManager); //this will create recycler view in linear layout.
//
//        mSubjectsAdapter = new SubjectsAdapter(this, getSubjectsList());
//        mSubjectsRecyclerView.setAdapter(mSubjectsAdapter);

        return root;
    }
    private ArrayList<CoursesModel> getCoursesList() {

        ArrayList<CoursesModel> models = new ArrayList<>();

        CoursesModel m = new CoursesModel();
        m.setStreamTitle("Computer Science");
        models.add(m);

        m = new CoursesModel();
        m.setStreamTitle("Mechanical Engineering");
        models.add(m);

        m = new CoursesModel();
        m.setStreamTitle("Competitive Exams");
        models.add(m);

        m = new CoursesModel();
        m.setStreamTitle("Others");
        models.add(m);

        return  models;
    }

    private ArrayList<SubjectsModel> getSubjectsList() {

        ArrayList<SubjectsModel> subjectList = new ArrayList<>();

        SubjectsModel m = new SubjectsModel();
        m.setSubjectTitle("C Programming");
        m.setImg(R.drawable.eduzap_logo_verysmall);
        subjectList.add(m);

        m = new SubjectsModel();
        m.setSubjectTitle("Data Structures");
        m.setImg(R.drawable.eduzap_logo_verysmall);
        subjectList.add(m);

        m = new SubjectsModel();
        m.setSubjectTitle("Database");
        m.setImg(R.drawable.eduzap_logo_verysmall);
        subjectList.add(m);

        return  subjectList;
    }
}

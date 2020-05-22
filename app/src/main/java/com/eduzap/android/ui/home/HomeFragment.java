package com.eduzap.android.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.home.Adapter.CoursesAdapter;
import com.eduzap.android.ui.home.Interface.IFirebaseLoadListener;
import com.eduzap.android.ui.home.Model.CoursesModel;
import com.eduzap.android.ui.home.Model.SubjectsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment implements IFirebaseLoadListener {

    private HomeViewModel homeViewModel;
    AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;

    RecyclerView my_recycler_view;

    DatabaseReference myData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Init
        myData = FirebaseDatabase.getInstance().getReference("Courses");
        dialog = new SpotsDialog.Builder().setContext(this.getActivity()).build();
        iFirebaseLoadListener = this;

        //View
        my_recycler_view = root.findViewById(R.id.coursesRecyclerView);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //load data
        getFirebaseData();

        return root;
    }

    private void getFirebaseData() {
        dialog.show();
        myData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CoursesModel> coursesModels = new ArrayList<>();
                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    CoursesModel coursesModel = new CoursesModel();
                    coursesModel.setCourseTitle(groupSnapShot.child("CourseTitle").getValue(true).toString());
                    GenericTypeIndicator<ArrayList<SubjectsModel>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<SubjectsModel>>() {
                    };
                    coursesModel.setSubjectItem(groupSnapShot.child("SubjectItem").getValue(genericTypeIndicator));
                    coursesModels.add(coursesModel);
                }
                iFirebaseLoadListener.onFirebaseLoadSuccess(coursesModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadListener.FirebaseLoadFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<CoursesModel> coursesModelList) {
        CoursesAdapter adapter = new CoursesAdapter(this.getActivity(), coursesModelList);
        my_recycler_view.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void FirebaseLoadFailed(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}

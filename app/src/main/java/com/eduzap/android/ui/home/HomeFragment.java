package com.eduzap.android.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.home.Adapter.CoursesAdapter;
import com.eduzap.android.ui.home.Adapter.SliderAdapter;
import com.eduzap.android.ui.home.Interface.IFirebaseLoadListener;
import com.eduzap.android.ui.home.Model.CoursesModel;
import com.eduzap.android.ui.home.Model.SliderItem;
import com.eduzap.android.ui.home.Model.SubjectsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements IFirebaseLoadListener {

    private HomeViewModel homeViewModel;
    //AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;
    RecyclerView my_recycler_view;
    ProgressBar progressBar;
    DatabaseReference myData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.homeProgressBar);

        //image slider
        final SliderView sliderView = root.findViewById(R.id.imageSlider);


        SliderAdapter adapter = new SliderAdapter(this.getActivity());
        adapter.addItem(new SliderItem("Demo Image 1", "https://quotefancy.com/media/wallpaper/1600x900/10414-Leonardo-da-Vinci-Quote-Time-stays-long-enough-for-those-who-use.jpg"));
        adapter.addItem(new SliderItem("Demo Image 2", "https://quotefancy.com/media/wallpaper/1600x900/10317-Leonardo-da-Vinci-Quote-Learning-never-exhausts-the-mind.jpg"));
        adapter.addItem(new SliderItem("Demo Image 3", "https://quotefancy.com/media/wallpaper/3840x2160/10256-Leonardo-da-Vinci-Quote-Study-without-desire-spoils-the-memory-and.jpg"));
        adapter.addItem(new SliderItem("Demo Image 4", "https://quotefancy.com/media/wallpaper/3840x2160/24042-Albert-Einstein-Quote-Any-fool-can-know-The-point-is-to-understand.jpg"));

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(8);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
            }
        });

        //Init
        myData = FirebaseDatabase.getInstance().getReference("Courses");
        //dialog = new SpotsDialog.Builder().setContext(this.getActivity()).build();
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
        //dialog.show();
        progressBar.setVisibility(View.VISIBLE);

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

        //dialog.dismiss();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void FirebaseLoadFailed(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
        //dialog.dismiss();
        progressBar.setVisibility(View.GONE);

    }


}

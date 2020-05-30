package com.eduzap.android.ui.bottom_navigation.videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.eduzap.android.R;

public class VideosFragment extends Fragment {

    private VideosViewModel videosViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videosViewModel =
                ViewModelProviders.of(this).get(VideosViewModel.class);
        View root = inflater.inflate(R.layout.bottom_nav_fragment_videos, container, false);

        String course = getActivity().getIntent().getStringExtra("course_name");
        String subject = getActivity().getIntent().getStringExtra("subject_name");

        int coursePosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int subjectPosition = getActivity().getIntent().getIntExtra("subject_position", 0);

        TextView textView = root.findViewById(R.id.tv);
        String text = "Course name: " + course + "\nCourse Position: " + coursePosition + "\nSubject Name: " + subject + "\nSubject Position: " + subjectPosition;
        textView.setText(text);

        return root;
    }
}

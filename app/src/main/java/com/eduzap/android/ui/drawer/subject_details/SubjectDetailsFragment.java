package com.eduzap.android.ui.drawer.subject_details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

public class SubjectDetailsFragment extends Fragment {

    RecyclerView subject_details_recycler_view;
    ProgressBar progressBar;
    private SubjectDetailsViewModel mViewModel;

    public static SubjectDetailsFragment newInstance() {
        return new SubjectDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.subject_details_fragment, container, false);

        progressBar = root.findViewById(R.id.subjectDetailsProgressBar);
        subject_details_recycler_view = root.findViewById(R.id.subjectDetailsRecyclerView);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SubjectDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}
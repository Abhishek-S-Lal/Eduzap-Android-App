package com.eduzap.android.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

public class CoursesHolder extends RecyclerView.ViewHolder {

    TextView mStreamTitle;
    RecyclerView mSubjectsRecyclerView;

    public CoursesHolder(@NonNull View itemView) {
        super(itemView);
        this.mStreamTitle = itemView.findViewById(R.id.streamName);
        this.mSubjectsRecyclerView = itemView.findViewById(R.id.subjectsRecyclerView);
    }
}

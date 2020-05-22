package com.eduzap.android.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

public class SubjectsHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mSubjectTitle;

    public SubjectsHolder(@NonNull View itemView) {
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.subjectImage);
        this.mSubjectTitle = itemView.findViewById(R.id.subjectName);

    }
}

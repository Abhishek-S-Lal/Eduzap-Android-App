package com.eduzap.android.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

import java.util.ArrayList;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsHolder> {

    Context c;
    ArrayList<SubjectsModel> models; //this array list create a list of array which parameters define in our model class.

    public SubjectsAdapter(HomeFragment c, ArrayList<SubjectsModel> models) {
        this.c = c.getActivity();
        this.models = models;
    }

    @NonNull
    @Override
    public SubjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjects,null); //this will infiltrate the row.

        return new SubjectsHolder(view); //this will return our view to holder class.
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectsHolder holder, int position) {

        holder.mSubjectTitle.setText(models.get(position).getSubjectTitle());
        holder.mImageView.setImageResource(models.get(position).getImg()); //here we use image resource because we will be using imager in resource folder which is drawable.

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

package com.eduzap.android.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesHolder> {

    Context c;

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    ArrayList<CoursesModel> models; //this array list create a list of array which parameters define in our model class.

    public CoursesAdapter(HomeFragment c, ArrayList<CoursesModel> models) {
        this.c = c.getActivity();
        this.models = models;
    }

    @NonNull
    @Override
    public CoursesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses, null); //this line inflate our row.

        return new CoursesHolder(view); //this will return our view to holder class.
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesHolder holder, int position) {
        CoursesModel item = models.get(position);
        holder.mStreamTitle.setText(models.get(position).getStreamTitle());

        //Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.mSubjectsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.get);///////////////////////////////////
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}

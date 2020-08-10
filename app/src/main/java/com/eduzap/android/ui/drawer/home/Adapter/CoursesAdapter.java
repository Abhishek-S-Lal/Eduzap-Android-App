package com.eduzap.android.ui.drawer.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.home.Model.CoursesModel;
import com.eduzap.android.ui.drawer.home.Model.SubjectsModel;
import com.eduzap.android.ui.drawer.subject_details.SubjectDetailsActivity;

import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyViewHolder> {

    private Context context;
    private List<CoursesModel> dataList;

    public CoursesAdapter(Context context, List<CoursesModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recycler_courses_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        String courseTitle = dataList.get(position).getCourseTitle();
        holder.CourseTitle.setText(courseTitle);
        List<SubjectsModel> itemData = dataList.get(position).getSubjectItem();
        SubjectAdapter itemListAdapter = new SubjectAdapter(context, itemData, courseTitle, position);
        holder.subjects_recyclerView_item_list.setHasFixedSize(true);
        holder.subjects_recyclerView_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.subjects_recyclerView_item_list.setAdapter(itemListAdapter);

        holder.subjects_recyclerView_item_list.setNestedScrollingEnabled(false); //important


        //Button More
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Intent intent = new Intent(context, SubjectDetailsActivity.class);
                intent.putExtra("courseName", courseTitle);
                intent.putExtra("coursePosition", position);
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CourseTitle;
        RecyclerView subjects_recyclerView_item_list;
        Button btn_more;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            CourseTitle = itemView.findViewById(R.id.streamName);
            btn_more = itemView.findViewById(R.id.btnMore);
            subjects_recyclerView_item_list = itemView.findViewById(R.id.subjectsRecyclerView);
        }
    }
}

package com.eduzap.android.ui.drawer.subject_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.bottom_navigation.LearnActivity;
import com.eduzap.android.ui.drawer.home.Interface.IItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubjectDetailsAdapter extends RecyclerView.Adapter<SubjectDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<SubjectDetailsModel> subjectDetailsList;
    private String courseName;
    private int coursePosition;

    public SubjectDetailsAdapter(Context context, ArrayList<SubjectDetailsModel> subjectDetailsList, String courseName, int coursePosition) {
        this.context = context;
        this.subjectDetailsList = subjectDetailsList;
        this.courseName = courseName;
        this.coursePosition = coursePosition;
    }

    @NonNull
    @Override
    public SubjectDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_subject_details_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectDetailsAdapter.MyViewHolder holder, int position) {
        holder.subjectName.setText(subjectDetailsList.get(position).getSubjectName());
        holder.subjectDescription.setText(subjectDetailsList.get(position).getSubjectDescription());
        holder.teacherName.setText(subjectDetailsList.get(position).getTeacherName());
        Picasso.get().load(subjectDetailsList.get(position).getSubjectImage()).into(holder.subjectImage);

        //Don't forget to implement item click
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

                Intent intent = new Intent(context, LearnActivity.class);
                Bundle b = new Bundle();

                //get text for current item
                String subjectName = subjectDetailsList.get(position).getSubjectName();
                //put extra into a bundle and add to intent
                //get position to carry integer
                intent.putExtra("subject_name", subjectName);
                intent.putExtra("subject_position", position);
                intent.putExtra("course_position", coursePosition);

                intent.putExtras(b);
                //begin activity
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (subjectDetailsList != null ? subjectDetailsList.size() : 0);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subjectName;
        TextView subjectDescription;
        ImageView subjectImage;
        TextView teacherName;
        TextView teacherMobile;

        IItemClickListener iItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectDescription = itemView.findViewById(R.id.subjectDescription);
            subjectImage = itemView.findViewById(R.id.subjectImage);
            teacherName = itemView.findViewById(R.id.teacherName);

            itemView.setOnClickListener(this);
        }

        public void setiItemClickListener(IItemClickListener iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        @Override
        public void onClick(View v) {
            iItemClickListener.onItemClickListener(v, getAdapterPosition());
        }
    }
}

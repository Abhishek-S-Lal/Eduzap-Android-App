package com.eduzap.android.ui.drawer.home.Adapter;

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

import com.eduzap.android.LearnActivity;
import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.home.Interface.IItemClickListener;
import com.eduzap.android.ui.drawer.home.Model.SubjectsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private Context context;
    private List<SubjectsModel> subjectsModelList;
    private String courseName;
    private int coursePosition;


    public SubjectAdapter(Context context, List<SubjectsModel> subjectsModelList, String courseName, int coursePosition) {
        this.context = context;
        this.subjectsModelList = subjectsModelList;
        this.courseName = courseName;
        this.coursePosition = coursePosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.subjects, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.text_item_title.setText(subjectsModelList.get(position).getName());
        Picasso.get().load(subjectsModelList.get(position).getImage()).into(holder.image_item);

        //Don't forget to implement item click
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

                Intent intent = new Intent(context, LearnActivity.class);
                Bundle b = new Bundle();

                //get text for current item
                String subjectName = subjectsModelList.get(position).getName();
                //put text into a bundle and add to intent
                intent.putExtra("course_name", courseName);
                intent.putExtra("subject_name", subjectName);

                //get position to carry integer
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
        return (subjectsModelList != null ? subjectsModelList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_item_title;
        ImageView image_item;

        IItemClickListener iItemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            text_item_title = itemView.findViewById(R.id.subjectName);
            image_item = itemView.findViewById(R.id.subjectImage);

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

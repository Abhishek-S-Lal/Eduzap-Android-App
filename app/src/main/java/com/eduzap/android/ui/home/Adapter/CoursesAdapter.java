package com.eduzap.android.ui.home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.home.Model.CoursesModel;
import com.eduzap.android.ui.home.Model.SubjectsModel;

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
        View itemView = LayoutInflater.from(context).inflate(R.layout.courses, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.CourseTitle.setText(dataList.get(position).getCourseTitle());
        List<SubjectsModel> itemData = dataList.get(position).getSubjectItem();
        SubjectAdapter itemListAdapter = new SubjectAdapter(context, itemData);
        holder.recyclerView_item_list.setHasFixedSize(true);
        holder.recyclerView_item_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView_item_list.setAdapter(itemListAdapter);

        holder.recyclerView_item_list.setNestedScrollingEnabled(false); //important

        //Button More
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button More : " + holder.CourseTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CourseTitle;
        RecyclerView recyclerView_item_list;
        Button btn_more;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            CourseTitle = itemView.findViewById(R.id.streamName);
            btn_more = itemView.findViewById(R.id.btnMore);
            recyclerView_item_list = itemView.findViewById(R.id.subjectsRecyclerView);
        }
    }
}

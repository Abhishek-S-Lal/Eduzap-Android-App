package com.eduzap.android.ui.drawer.downloads;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_download_list, parent, false);
        return new FileLayoutHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FileLayoutHolder) holder).downloads_title.setText(Constant.allMediaList.get(position).getName());
        //we will load thumbnail using glid library
        Uri uri = Uri.fromFile(Constant.allMediaList.get(position));
        ((FileLayoutHolder) holder).downloads_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return Constant.allMediaList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class FileLayoutHolder extends RecyclerView.ViewHolder {

        TextView downloads_title;
        Button downloads_view;

        public FileLayoutHolder(@NonNull View itemView) {
            super(itemView);

            downloads_title = itemView.findViewById(R.id.document_title);
            downloads_view = itemView.findViewById(R.id.downloads_view_btn);

        }
    }
}

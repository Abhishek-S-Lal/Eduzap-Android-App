package com.eduzap.android.ui.drawer.downloads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.BuildConfig;
import com.eduzap.android.R;

import java.io.File;
import java.util.ArrayList;

public class DownloadsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<File> DocumentsList;

    DownloadsAdapter(Context mContext, ArrayList<File> DocumentsList) {
        this.mContext = mContext;
        this.DocumentsList = DocumentsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_download_list_item, parent, false);
        return new FileLayoutHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FileLayoutHolder) holder).downloads_title.setText(DocumentsList.get(position).getName());
        //we will load thumbnail using glid library
        Uri uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", DocumentsList.get(position));
        ((FileLayoutHolder) holder).btn_downloads_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (DocumentsList.get(position).toString().contains(".doc") || DocumentsList.get(position).toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                } else if (DocumentsList.get(position).toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                } else if (DocumentsList.get(position).toString().contains(".ppt") || DocumentsList.get(position).toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (DocumentsList.get(position).toString().contains(".xls") || DocumentsList.get(position).toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (DocumentsList.get(position).toString().contains(".zip") || DocumentsList.get(position).toString().contains(".rar")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "application/x-wav");
                } else if (DocumentsList.get(position).toString().contains(".rtf")) {
                    // RTF file
                    intent.setDataAndType(uri, "application/rtf");
                } else if (DocumentsList.get(position).toString().contains(".wav") || DocumentsList.get(position).toString().contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (DocumentsList.get(position).toString().contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(uri, "image/gif");
                } else if (DocumentsList.get(position).toString().contains(".jpg") || DocumentsList.get(position).toString().contains(".jpeg") || DocumentsList.get(position).toString().contains(".png")) {
                    // JPG file
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (DocumentsList.get(position).toString().contains(".txt")) {
                    // Text file
                    intent.setDataAndType(uri, "text/plain");
                } else if (DocumentsList.get(position).toString().contains(".3gp") || DocumentsList.get(position).toString().contains(".mpg") || DocumentsList.get(position).toString().contains(".mpeg") || DocumentsList.get(position).toString().contains(".mpe") || DocumentsList.get(position).toString().contains(".mp4") || DocumentsList.get(position).toString().contains(".avi")) {
                    // Video files
                    intent.setDataAndType(uri, "video/*");
                } else {
                    //if you want you can also define the intent type for any other file
                    //additionally use else clause below, to manage other unknown extensions
                    //in this case, Android will show all applications installed on the device
                    //so you can choose which application to use
                    intent.setDataAndType(uri, "*/*");
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return DocumentsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class FileLayoutHolder extends RecyclerView.ViewHolder {

        TextView downloads_title;
        Button btn_downloads_open, btn_downloads_delete;

        public FileLayoutHolder(@NonNull View itemView) {
            super(itemView);

            downloads_title = itemView.findViewById(R.id.document_title);
            btn_downloads_open = itemView.findViewById(R.id.downloads_open_btn);
        }
    }
}

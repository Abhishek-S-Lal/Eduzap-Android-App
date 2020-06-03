package com.eduzap.android.ui.bottom_navigation.documents;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.home.Interface.IItemClickListener;

import java.io.File;
import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.MyViewHolder> {
    Context context;
    ArrayList<DocumentListModel> documentItem;
    String url;
    int documentPosition = 0;

    public DocumentListAdapter(Context context, ArrayList<DocumentListModel> documentItem) {
        this.context = context;
        this.documentItem = documentItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_document_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.documentName.setText(documentItem.get(position).getDocumentName());
        holder.documentDescription.setText(documentItem.get(position).getDocumentDescription());
        holder.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(context, documentItem.get(position).getDocumentName(), ".pdf", "/Eduzap_Documents", documentItem.get(position).getDocumentUrl());
                Toast.makeText(context, "Downloading..", Toast.LENGTH_SHORT).show();
            }
        });

        //Don't forget to implement item click
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, final int position) {
                Toast.makeText(context, "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        String storagePath = Environment.getExternalStorageDirectory()
                .getPath()
                + "/Eduzap_Documents/";
        //Log.d("Strorgae in view",""+storagePath);
        File f = new File(storagePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        //storagePath.mkdirs();
        String pathname = f.toString();
        if (!f.exists()) {
            f.mkdirs();
        }
//                Log.d("Storage ",""+pathname);

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }


    @Override
    public int getItemCount() {
        return documentItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView documentName;
        TextView documentDescription;
        Button downloadButton;

        IItemClickListener iItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            documentName = itemView.findViewById(R.id.document_name);
            documentDescription = itemView.findViewById(R.id.document_description);
            downloadButton = itemView.findViewById(R.id.download_button);


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

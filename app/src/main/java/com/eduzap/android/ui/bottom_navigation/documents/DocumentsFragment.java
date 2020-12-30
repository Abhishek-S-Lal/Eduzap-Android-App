package com.eduzap.android.ui.bottom_navigation.documents;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DocumentsFragment extends Fragment {

    Context context;
    Query query;
    ArrayList<DocumentListModel> list;
    DocumentListAdapter adapter;
    RecyclerView documentRecyclerView;
    ProgressBar progressBar;
    TextView documentName, documentDescription;
    private ValueEventListener documentListListener;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = adapter.getDownloadmanager().query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    Toast.makeText(context, "Download Completed. You can view your downloads from the menu.", Toast.LENGTH_LONG).show();
                    String file_title = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                } else if (status == DownloadManager.STATUS_FAILED) {
                    Toast.makeText(context, "Download Failed.", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        int cPosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int sPosition = getActivity().getIntent().getIntExtra("subject_position", 0);
        String subject = getActivity().getIntent().getStringExtra("subject_name");

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        getActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        final View root = inflater.inflate(R.layout.bottom_nav_fragment_documents, container, false);

        //no connection
        if (!InternetConnection.checkConnection(getContext())) {
            LinearLayout noInternet = root.findViewById(R.id.documents_no_internet);
            noInternet.setVisibility(View.VISIBLE);
            Button retry = root.findViewById(R.id.documents_retry);
            retry.setText("Go Back");
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //this will result in go back
                    getActivity().finish();
                }
            });

            return root;
        }

        //document fragment continues
        documentName = root.findViewById(R.id.document_name);
        documentDescription = root.findViewById(R.id.document_description);

        progressBar = root.findViewById(R.id.documentProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        documentRecyclerView = root.findViewById(R.id.documentsRecyclerView);
        documentRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        query = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("documents");
        documentListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<DocumentListModel>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DocumentListModel documentListItem = new DocumentListModel();

                    try {
                        documentListItem.setDocumentName(snapshot.child("name").getValue(true).toString());
                        documentListItem.setDocumentDescription(snapshot.child("description").getValue(true).toString());
                        documentListItem.setDocumentUrl(snapshot.child("url").getValue(true).toString());

                    }catch (Exception e){
                        System.out.println(e.toString());
                    }

                    list.add(documentListItem);
                }
                adapter = new DocumentListAdapter(getActivity(), list);
                adapter.notifyDataSetChanged();

                if (adapter.getItemCount() == 0) {
                    RelativeLayout container = root.findViewById(R.id.document_container);
                    container.setBackgroundColor(Color.WHITE);
                    TextView emptyTextViw = root.findViewById(R.id.emptyDocumentMsg);
                    ImageView emptyIV = root.findViewById(R.id.empty_document_img);
                    emptyIV.setVisibility(View.VISIBLE);
                    emptyTextViw.setText(R.string.empty_documents_message);
                    emptyTextViw.setVisibility(View.VISIBLE);
                }
                documentRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Oops.... Something is wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        query.addListenerForSingleValueEvent(documentListListener);


        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onDownloadComplete != null) {
            getActivity().unregisterReceiver(onDownloadComplete);
        }

    }

}

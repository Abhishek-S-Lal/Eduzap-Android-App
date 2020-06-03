package com.eduzap.android.ui.bottom_navigation.documents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DocumentsFragment extends Fragment {

    private DocumentsViewModel documentsViewModel;

    DatabaseReference reference;
    ArrayList<DocumentListModel> list;
    DocumentListAdapter adapter;
    RecyclerView documentRecyclerView;
    ProgressBar progressBar;
    TextView documentName, documentDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        documentsViewModel =
                ViewModelProviders.of(this).get(DocumentsViewModel.class);
        final View root = inflater.inflate(R.layout.bottom_nav_fragment_documents, container, false);

        //

        int cPosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int sPosition = getActivity().getIntent().getIntExtra("subject_position", 0);

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        documentName = root.findViewById(R.id.document_name);
        documentDescription = root.findViewById(R.id.document_description);

        progressBar = root.findViewById(R.id.documentProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        documentRecyclerView = root.findViewById(R.id.documentsRecyclerView);
        documentRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        reference = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("documents");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<DocumentListModel>();

                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    DocumentListModel documentListModel = new DocumentListModel();

                    documentListModel.setDocumentName(groupSnapShot.child("name").getValue(true).toString());
                    documentListModel.setDocumentDescription(groupSnapShot.child("description").getValue(true).toString());
                    documentListModel.setDocumentUrl(groupSnapShot.child("url").getValue(true).toString());


                    list.add(documentListModel);
                }
                adapter = new DocumentListAdapter(getActivity(), list);
                if (adapter.getItemCount() == 0) {
                    TextView emptyTextViw = root.findViewById(R.id.emptyDocumentMsg);
                    emptyTextViw.setText(R.string.empty_documents_message);
                    emptyTextViw.setVisibility(View.VISIBLE);
                }

                documentRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        //
        return root;
    }
}

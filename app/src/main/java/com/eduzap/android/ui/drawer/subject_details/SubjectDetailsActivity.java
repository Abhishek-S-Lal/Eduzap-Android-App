package com.eduzap.android.ui.drawer.subject_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class SubjectDetailsActivity extends AppCompatActivity {


    private SubjectDetailsViewModel mViewModel;
    private RecyclerView subject_details_recycler_view;
    private ProgressBar progressBar;
    private Query query;
    private ArrayList<SubjectDetailsModel> list;
    private SubjectDetailsAdapter adapter;
    private Context context;
    private ValueEventListener subjectDetailsListListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        int cPosition = this.getIntent().getIntExtra("coursePosition", 0);
        String coursePosition = Integer.toString(cPosition);
        String courseName = this.getIntent().getStringExtra("courseName");
        setTitle(courseName);

        if (!InternetConnection.checkConnection(context)) {
            setContentView(R.layout.no_internet);
            Button retry = findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!InternetConnection.checkConnection(context)) {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle b = new Bundle();
                        Intent intent = new Intent(context, SubjectDetailsActivity.class);
                        intent.putExtra("courseName", courseName);
                        intent.putExtra("coursePosition", cPosition);
                        intent.putExtras(b);
                        context.startActivity(intent);
                        finish();
                    }

                }
            });
        } else {

            setContentView(R.layout.activity_subject_details);


            progressBar = findViewById(R.id.subjectDetailsProgressBar);
            progressBar.setVisibility(View.VISIBLE);
            subject_details_recycler_view = findViewById(R.id.subjectDetailsRecyclerView);
            subject_details_recycler_view.setLayoutManager(new LinearLayoutManager(this));


            query = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem");
            subjectDetailsListListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubjectDetailsModel subjectDetailsListItem = new SubjectDetailsModel();

                        try {
                            subjectDetailsListItem.setSubjectName(snapshot.child("name").getValue(true).toString());
                            subjectDetailsListItem.setSubjectDescription(snapshot.child("description").getValue(true).toString());
                            subjectDetailsListItem.setTeacherName(snapshot.child("teacherName").getValue(true).toString());
                            subjectDetailsListItem.setSubjectImage(snapshot.child("image").getValue(true).toString());

                        }catch (Exception e){
                            System.out.println(e.toString());
                        }

                        list.add(subjectDetailsListItem);
                    }
                    adapter = new SubjectDetailsAdapter(context, list, courseName, cPosition);

                    //when the subject contains no videos
                    if (adapter.getItemCount() == 0) {
                        TextView emptyTextViw = findViewById(R.id.emptyVideosMsg);
                        emptyTextViw.setText(R.string.empty_videos_message);
                        emptyTextViw.setVisibility(View.VISIBLE);
                    }

                    subject_details_recycler_view.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, "Oops.... Something is wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            };
            query.addListenerForSingleValueEvent(subjectDetailsListListener);
        }

    }


}
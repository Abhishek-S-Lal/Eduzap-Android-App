package com.eduzap.android.ui.bottom_navigation.videos;

import android.content.Context;
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

public class VideosFragment extends Fragment {

    private RecyclerView videoRecyclerView;
    private ProgressBar progressBar;
    private Query query;
    private ArrayList<VideoListModel> list;
    private VideoListAdapter adapter;
    private ValueEventListener videoListListener;
    private Context context;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = this.getContext();
        int cPosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int sPosition = getActivity().getIntent().getIntExtra("subject_position", 0);
        String subject = getActivity().getIntent().getStringExtra("subject_name");

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        final View root = inflater.inflate(R.layout.bottom_nav_fragment_videos, container, false);

        //no connection
        if (!InternetConnection.checkConnection(getContext())) {
            LinearLayout noInternet = root.findViewById(R.id.videos_no_internet);
            noInternet.setVisibility(View.VISIBLE);
            Button retry = root.findViewById(R.id.videos_retry);
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

        //videos fragment continues
        progressBar = root.findViewById(R.id.videosProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        videoRecyclerView = root.findViewById(R.id.videosRecyclerView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        query = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("videos");
        videoListListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<VideoListModel>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VideoListModel videoListItem = new VideoListModel();

                    try{
                        videoListItem.setVideoName(snapshot.child("name").getValue(true).toString());
                        videoListItem.setVideoDescription(snapshot.child("description").getValue(true).toString());
                        videoListItem.setVideoThumbnail(snapshot.child("thumbnail").getValue(true).toString());
                        videoListItem.setVideoUrl(snapshot.child("url").getValue(true).toString());
                    }catch (Exception e){
                        System.out.println(e.toString());
                    }

                    list.add(videoListItem);
                }
                adapter = new VideoListAdapter(getActivity(), list, cPosition, sPosition);

                //when the subject contains no videos
                if (adapter.getItemCount() == 0) {
                    RelativeLayout container = root.findViewById(R.id.container);
                    container.setBackgroundColor(Color.WHITE);
                    TextView emptyTextViw = root.findViewById(R.id.emptyVideosMsg);
                    ImageView emptyIV = root.findViewById(R.id.empty_video_img);
                    emptyIV.setVisibility(View.VISIBLE);
                    emptyTextViw.setText(R.string.empty_videos_message);
                    emptyTextViw.setVisibility(View.VISIBLE);
                }

                videoRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Oops.... Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        query.addListenerForSingleValueEvent(videoListListener);


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!InternetConnection.checkConnection(getContext())) {
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.eduzap.android.ui.bottom_navigation.videos;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideosFragment extends Fragment {

    private VideosViewModel videosViewModel;
    private RecyclerView videoRecyclerView;
    private ProgressBar progressBar;
    private Query query;
    private ArrayList<VideoListModel> list;
    private VideoListAdapter adapter;
    private ValueEventListener videoListListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videosViewModel =
                ViewModelProviders.of(this).get(VideosViewModel.class);
        final View root = inflater.inflate(R.layout.bottom_nav_fragment_videos, container, false);

        int cPosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int sPosition = getActivity().getIntent().getIntExtra("subject_position", 0);

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);


        progressBar = root.findViewById(R.id.homeProgressBar);
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

                    videoListItem.setVideoName(snapshot.child("name").getValue(true).toString());
                    videoListItem.setVideoDescription(snapshot.child("description").getValue(true).toString());
                    videoListItem.setVideoThumbnail(snapshot.child("thumbnail").getValue(true).toString());
                    videoListItem.setVideoUrl(snapshot.child("url").getValue(true).toString());


                    list.add(videoListItem);
                }
                adapter = new VideoListAdapter(getActivity(), list, cPosition, sPosition);

                //when the subject contains no videos
                if (adapter.getItemCount() == 0) {
                    TextView emptyTextViw = root.findViewById(R.id.emptyVideosMsg);
                    emptyTextViw.setText(R.string.empty_videos_message);
                    emptyTextViw.setVisibility(View.VISIBLE);
                }

                videoRecyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Oops.... Something is wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        };
        query.addListenerForSingleValueEvent(videoListListener);
        return root;
    }

}

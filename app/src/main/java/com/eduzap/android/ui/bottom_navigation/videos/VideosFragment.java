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

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideosFragment extends Fragment {

    private VideosViewModel videosViewModel;

    DatabaseReference reference;
    ArrayList<VideoListModel> list;
    VideoListAdapter adapter;
    RecyclerView videoRecyclerView;
    ProgressBar progressBar;
    TextView videoName;
    ReadMoreTextView videoDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        videosViewModel =
                ViewModelProviders.of(this).get(VideosViewModel.class);
        final View root = inflater.inflate(R.layout.bottom_nav_fragment_videos, container, false);

        int cPosition = getActivity().getIntent().getIntExtra("course_position", 0);
        int sPosition = getActivity().getIntent().getIntExtra("subject_position", 0);

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        final YouTubePlayerView youTubePlayerView = root.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        videoName = root.findViewById(R.id.video_heading);
        videoDescription = root.findViewById(R.id.video_descrip);

        progressBar = root.findViewById(R.id.homeProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        videoRecyclerView = root.findViewById(R.id.videosRecyclerView);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        reference = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<VideoListModel>();

                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    VideoListModel videoListModel = new VideoListModel();

                    videoListModel.setVideoName(groupSnapShot.child("name").getValue(true).toString());
                    videoListModel.setVideoDescription(groupSnapShot.child("description").getValue(true).toString());
                    videoListModel.setVideoThumbnail(groupSnapShot.child("thumbnail").getValue(true).toString());
                    videoListModel.setVideoUrl(groupSnapShot.child("url").getValue(true).toString());


                    list.add(videoListModel);
                }
                adapter = new VideoListAdapter(getActivity(), list, youTubePlayerView, getLifecycle(), videoName, videoDescription);
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
        });
        return root;
    }

}

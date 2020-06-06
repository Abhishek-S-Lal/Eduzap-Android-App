package com.eduzap.android.ui;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {

    private YouTubePlayerView activityYouTubePlayerView;

    DatabaseReference reference;
    ArrayList<VideoPlayerListModel> list;
    VideoPlayerListAdapter adapter, adapter1;
    RecyclerView videoPlayerRecyclerView;
    ProgressBar videoPlayerProgressBar;
    TextView videoName;
    ReadMoreTextView videoDescription;
    String videoId;
    YouTubePlayer player;
    Lifecycle lifecycle;
    boolean ready = false;
    boolean callAdapter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initActivityYouTubePlayerView();

        //

        int cPosition = this.getIntent().getIntExtra("course_position", 0);
        int sPosition = this.getIntent().getIntExtra("subject_position", 0);
        int vPosition = this.getIntent().getIntExtra("video_position", 0);

        videoId = this.getIntent().getStringExtra("video_id");

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        videoName = findViewById(R.id.video_heading);
        videoDescription = findViewById(R.id.video_descrip);
        videoPlayerProgressBar = findViewById(R.id.videoPlayerProgressBar);
        videoPlayerProgressBar.setVisibility(View.VISIBLE);
        videoPlayerRecyclerView = findViewById(R.id.videoPlayerRecyclerView);
        videoPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<VideoPlayerListModel>();

                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    VideoPlayerListModel videoPlayerListModel = new VideoPlayerListModel();

                    videoPlayerListModel.setVideoName(groupSnapShot.child("name").getValue(true).toString());
                    videoPlayerListModel.setVideoDescription(groupSnapShot.child("description").getValue(true).toString());
                    videoPlayerListModel.setVideoThumbnail(groupSnapShot.child("thumbnail").getValue(true).toString());
                    videoPlayerListModel.setVideoUrl(groupSnapShot.child("url").getValue(true).toString());


                    list.add(videoPlayerListModel);
                }


                adapter = new VideoPlayerListAdapter(VideoPlayer.this, list, videoName, videoDescription);


                videoName.setText(list.get(vPosition).getVideoName());
                videoDescription.setText(list.get(vPosition).getVideoDescription());

                //when the subject contains no videos
                if (adapter.getItemCount() == 0) {
                    TextView emptyTextViw = findViewById(R.id.emptyVideoPlayerMsg);
                    emptyTextViw.setText("No videos to show");
                    emptyTextViw.setVisibility(View.VISIBLE);
                }


                videoPlayerRecyclerView.setAdapter(adapter);
                videoPlayerProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VideoPlayer.this, "Oops.... Something is wrong", Toast.LENGTH_SHORT).show();
                videoPlayerProgressBar.setVisibility(View.GONE);
            }
        });

        ///
    }

    private void initActivityYouTubePlayerView() {
        activityYouTubePlayerView = findViewById(R.id.activity_youtube_player_view);

        getLifecycle().addObserver(activityYouTubePlayerView);
        initPictureInPicture(activityYouTubePlayerView);
        lifecycle = getLifecycle();
        activityYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                adapter1 = new VideoPlayerListAdapter(player, lifecycle);
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        videoId, 0f
                );
            }
        });
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(this);
        pictureInPictureIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_picture_in_picture_24dp));

        pictureInPictureIcon.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if (supportsPIP)
                    enterPictureInPictureMode();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();
            }
        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);

        if (isInPictureInPictureMode) {
            activityYouTubePlayerView.enterFullScreen();
            activityYouTubePlayerView.getPlayerUiController().showUi(false);
        } else {
            activityYouTubePlayerView.exitFullScreen();
            activityYouTubePlayerView.getPlayerUiController().showUi(true);
        }
    }
}

package com.eduzap.android.ui.VideoPlayerPip;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.eduzap.android.InternetConnection;
import com.eduzap.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity{

    private YouTubePlayerView activityYouTubePlayerView;
    static boolean active = false;
    static boolean back = false;
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
    int cPosition, sPosition, vPosition;
    Context context;
    Boolean playerReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        cPosition = this.getIntent().getIntExtra("course_position", 0);
        sPosition = this.getIntent().getIntExtra("subject_position", 0);
        vPosition = this.getIntent().getIntExtra("video_position", 0);
        videoId = this.getIntent().getStringExtra("video_id");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //no connection
        if (!InternetConnection.checkConnection(this)) {
            setContentView(R.layout.no_internet);
            Button retry = findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!InternetConnection.checkConnection(context)) {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, VideoPlayer.class);
                        Bundle b = new Bundle();

                        //put extra into a bundle and add to intent
                        //get position to carry integer
                        intent.putExtra("subject_position", sPosition);
                        intent.putExtra("course_position", cPosition);
                        intent.putExtra("video_position", vPosition);
                        intent.putExtra("video_id", videoId);
                        intent.putExtras(b);
                        //begin activity
                        context.startActivity(intent);

                    }

                }
            });
        } else {
            setContentView(R.layout.activity_video_player);

            videoName = findViewById(R.id.video_heading);
            videoDescription = findViewById(R.id.video_descrip);
            videoPlayerProgressBar = findViewById(R.id.videoPlayerProgressBar);
            videoPlayerProgressBar.setVisibility(View.VISIBLE);
            videoPlayerRecyclerView = findViewById(R.id.videoPlayerRecyclerView);
            videoPlayerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            activityYouTubePlayerView = findViewById(R.id.activity_youtube_player_view);
            getLifecycle().addObserver(activityYouTubePlayerView);
            initPictureInPicture(activityYouTubePlayerView);
            lifecycle = getLifecycle();

            startVideo();
        }
    }

    private void startVideo() {

        String subjectPosition = Integer.toString(sPosition);
        String coursePosition = Integer.toString(cPosition);

        //hooks
        reference = FirebaseDatabase.getInstance().getReference().child("Courses").child(coursePosition).child("SubjectItem").child(subjectPosition).child("videos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<VideoPlayerListModel>();

                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    VideoPlayerListModel videoPlayerListModel = new VideoPlayerListModel();

                    try{
                        videoPlayerListModel.setVideoName(groupSnapShot.child("name").getValue(true).toString());
                        videoPlayerListModel.setVideoDescription(groupSnapShot.child("description").getValue(true).toString());
                        videoPlayerListModel.setVideoThumbnail(groupSnapShot.child("thumbnail").getValue(true).toString());
                        videoPlayerListModel.setVideoUrl(groupSnapShot.child("url").getValue(true).toString());
                    }
                    catch (Exception e){
                        System.out.println(e.toString());
                    }
                    list.add(videoPlayerListModel);
                }

                adapter = new VideoPlayerListAdapter(VideoPlayer.this, list, videoName, videoDescription, activityYouTubePlayerView, lifecycle, player, videoId);
                //adapter.notifyDataSetChanged();

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
    }

    private void initPictureInPicture(YouTubePlayerView youTubePlayerView) {
        ImageView pictureInPictureIcon = new ImageView(this);
        pictureInPictureIcon.setVisibility(View.GONE);
        pictureInPictureIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_picture_in_picture));

        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            pictureInPictureIcon.setVisibility(View.VISIBLE);
            playerReady=true;
        });
        pictureInPictureIcon.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if (supportsPIP) {
                    enterPictureInPictureMode();

                }

            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Can't enter picture in picture mode")
                        .setMessage("In order to enter picture in picture mode you need a SDK version >= N.")
                        .show();

            }
        });

        youTubePlayerView.getPlayerUiController().addView(pictureInPictureIcon);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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


    @Override //this function will call when second time intending since launch mode is single task
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        finish(); //we will destroy the current instance because we dont want the old state
        cPosition = intent.getIntExtra("course_position", 0);
        sPosition = intent.getIntExtra("subject_position", 0);
        vPosition = intent.getIntExtra("video_position", 0);
        videoId = intent.getStringExtra("video_id");

        //intend the same activity with the new values
        Intent i = getIntent();
        Bundle b = new Bundle();
        //put extra into a bundle and add to intent
        //get position to carry integer
        i.putExtra("subject_position", sPosition);
        i.putExtra("course_position", cPosition);
        i.putExtra("video_position", vPosition);
        i.putExtra("video_id", videoId);
        i.putExtras(b);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStop() {
        super.onStop();
        finishAndRemoveTask();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityYouTubePlayerView.release();
    }

    @Override
    public void onBackPressed() {
        if (!playerReady){
            super.onBackPressed();
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                boolean supportsPIP = getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
                if (supportsPIP) {
                    enterPictureInPictureMode();
                }
                else {
                    super.onBackPressed();
                }

            }
        }

    }
}

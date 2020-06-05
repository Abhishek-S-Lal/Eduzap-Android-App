package com.eduzap.android.ui;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.eduzap.android.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayer extends AppCompatActivity {

    private YouTubePlayerView activityYouTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initActivityYouTubePlayerView();
    }

    private void initActivityYouTubePlayerView() {
        activityYouTubePlayerView = findViewById(R.id.activity_youtube_player_view);

        getLifecycle().addObserver(activityYouTubePlayerView);
        initPictureInPicture(activityYouTubePlayerView);

        activityYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        "j5swuGl3bDg", 0f
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

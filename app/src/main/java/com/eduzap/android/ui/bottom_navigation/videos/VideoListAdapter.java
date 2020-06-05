package com.eduzap.android.ui.bottom_navigation.videos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.home.Interface.IItemClickListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    Context context;
    ArrayList<VideoListModel> videoItem;
    YouTubePlayerView youTubePlayerView;
    Lifecycle lifecycle;
    String videoId;
    TextView videoName;
    ReadMoreTextView videoDescription;
    YouTubePlayer player;
    int videoPosition = 0;
    float videoStartPostion = 0f;

    public VideoListAdapter(Context context, ArrayList<VideoListModel> videoItem, YouTubePlayerView youTubePlayerView, Lifecycle lifecycle, TextView videoName, ReadMoreTextView videoDescription) {
        this.context = context;
        this.videoItem = videoItem;
        this.youTubePlayerView = youTubePlayerView;
        this.lifecycle = lifecycle;
        this.videoName = videoName;
        this.videoDescription = videoDescription;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        initYouTubePlayerView();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_video_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.videoName.setText(videoItem.get(position).getVideoName());
        holder.videoDescription.setText(videoItem.get(position).getVideoDescription());
        Picasso.get().load(videoItem.get(position).getVideoThumbnail()).into(holder.videoThumbnail);

        //Don't forget to implement item click
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, final int position) {
                videoPosition = position;
                if (player != null) {
                    videoId = videoItem.get(videoPosition).getVideoUrl();
                    videoName.setText(videoItem.get(videoPosition).getVideoName());
                    videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());
                    YouTubePlayerUtils.loadOrCueVideo(
                            player, lifecycle,
                            videoId, 0f
                    );
                } else {
                    videoName.setText(videoItem.get(videoPosition).getVideoName());
                    videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());
                }
            }
        });
    }

    private void initYouTubePlayerView() {
        // The player will automatically release itself when the fragment is destroyed.
        // The player will automatically pause when the fragment is stopped
        // If you don't add YouTubePlayerView as a lifecycle observer, you will have to release it manually.

        videoName.setText(videoItem.get(videoPosition).getVideoName());
        videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                player = youTubePlayer;
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, lifecycle,
                        videoItem.get(videoPosition).getVideoUrl(), videoStartPostion
                );
            }
        });

    }


    @Override
    public int getItemCount() {
        return videoItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView videoName;
        TextView videoDescription;
        ImageView videoThumbnail;

        IItemClickListener iItemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            videoName = itemView.findViewById(R.id.video_name);
            videoDescription = itemView.findViewById(R.id.video_description);
            videoThumbnail = itemView.findViewById(R.id.video_thumbnail);

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

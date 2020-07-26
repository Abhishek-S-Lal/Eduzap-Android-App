package com.eduzap.android.ui;

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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoPlayerListAdapter extends RecyclerView.Adapter<VideoPlayerListAdapter.MyViewHolder> {
    Context context;
    ArrayList<VideoPlayerListModel> videoItem;
    int videoPosition = 0;
    YouTubePlayer player;
    String videoId;
    TextView videoName;
    ReadMoreTextView videoDescription;
    Lifecycle lifecycle;
    YouTubePlayerTracker tracker;

    private YouTubePlayerView activityYouTubePlayerView;


    public VideoPlayerListAdapter(Context context, ArrayList<VideoPlayerListModel> videoItem, TextView videoName, ReadMoreTextView videoDescription, YouTubePlayerView activityYouTubePlayerView, Lifecycle lifecycle, YouTubePlayer player, String videoId) {
        this.context = context;
        this.videoItem = videoItem;
        this.videoName = videoName;
        this.videoDescription = videoDescription;

        this.activityYouTubePlayerView = activityYouTubePlayerView;
        this.lifecycle = lifecycle;
        this.player = player;
        this.videoId = videoId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_video_player_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.videoName.setText(videoItem.get(position).getVideoName());
        holder.videoDescription.setText(videoItem.get(position).getVideoDescription());
        Picasso.get().load(videoItem.get(position).getVideoThumbnail()).into(holder.videoThumbnail);


        if (player == null) {
            //init youtube player
            activityYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    player = youTubePlayer;

                    YouTubePlayerUtils.loadOrCueVideo(
                            youTubePlayer, lifecycle,
                            videoId, 0f
                    );

                }
            });

        } else {
            YouTubePlayerUtils.loadOrCueVideo(
                    player, lifecycle,
                    videoId, 0f
            );

        }

        //Don't forget to implement item click
        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, final int position) {
                videoPosition = position;
                videoId = videoItem.get(videoPosition).getVideoUrl();

                videoName.setText(videoItem.get(videoPosition).getVideoName());
                videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());


                if (player != null) {

                    YouTubePlayerUtils.loadOrCueVideo(
                            player, lifecycle,
                            videoId, 0f
                    );

                } else {

                    activityYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            player = youTubePlayer;

                            YouTubePlayerUtils.loadOrCueVideo(
                                    youTubePlayer, lifecycle,
                                    videoId, 0f
                            );
                        }
                    });
                }
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
            videoName = itemView.findViewById(R.id.video_player_list_name);
            videoDescription = itemView.findViewById(R.id.video_player_list_description);
            videoThumbnail = itemView.findViewById(R.id.video_player_list_thumbnail);

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

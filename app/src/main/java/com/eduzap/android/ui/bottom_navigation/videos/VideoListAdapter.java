package com.eduzap.android.ui.bottom_navigation.videos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eduzap.android.R;
import com.eduzap.android.ui.drawer.home.Interface.IItemClickListener;
import com.eduzap.android.ui.video_player_pip.VideoPlayer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    Context context;
    ArrayList<VideoListModel> videoItem;
    String videoId;
    private int coursePosition;
    private int subjectPosition;
    private int videoPosition;

    public VideoListAdapter(Context context, ArrayList<VideoListModel> videoItem, int coursePosition, int subjectPosition) {
        this.context = context;
        this.videoItem = videoItem;
        this.coursePosition = coursePosition;
        this.subjectPosition = subjectPosition;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

                videoId = videoItem.get(videoPosition).getVideoUrl();

                Intent intent = new Intent(context, VideoPlayer.class);
                Bundle b = new Bundle();


                //put extra into a bundle and add to intent
                //get position to carry integer
                intent.putExtra("subject_position", subjectPosition);
                intent.putExtra("course_position", coursePosition);
                intent.putExtra("video_position", videoPosition);
                intent.putExtra("video_id", videoId);
                intent.putExtras(b);
                //begin activity
                context.startActivity(intent);


//                if (player != null) {
//                    videoId = videoItem.get(videoPosition).getVideoUrl();
//                    videoName.setText(videoItem.get(videoPosition).getVideoName());
//                    videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());
//                    YouTubePlayerUtils.loadOrCueVideo(
//                            player, lifecycle,
//                            videoId, 0f
//                    );
//                } else {
//                    videoName.setText(videoItem.get(videoPosition).getVideoName());
//                    videoDescription.setText(videoItem.get(videoPosition).getVideoDescription());
//                }
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

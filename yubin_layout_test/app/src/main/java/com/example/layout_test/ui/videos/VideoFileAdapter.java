package com.example.layout_test.ui.videos;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.layout_test.R;

import java.io.File;
import java.util.ArrayList;

import static com.example.layout_test.ui.videos.VideoUtils.timeFormat;

public class VideoFileAdapter extends RecyclerView.Adapter<VideoFileHolder> {
    private String TAG = "201521037";
    public static ArrayList<VideoFile> videoFiles;
    private final Context mContext;
    public View view;

    public VideoFileAdapter(Context mContext, ArrayList<VideoFile> videoFiles) {
        this.mContext = mContext;
        VideoFileAdapter.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public VideoFileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_file_item, parent, false);
        return new VideoFileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFileHolder holder, int position) {
        holder.fileName.setText(videoFiles.get(position).getTitle());
        holder.videoDuration.setText(timeFormat(videoFiles.get(position).getDuration()));
        Glide.with(mContext)
                .load(new File(videoFiles.get(position).getPath()))
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayerActivity.class);
            intent.putExtra("position", position);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }

}

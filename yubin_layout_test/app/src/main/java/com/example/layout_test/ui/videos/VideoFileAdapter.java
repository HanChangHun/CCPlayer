package com.example.layout_test.ui.videos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.layout_test.R;

import java.io.File;
import java.util.ArrayList;

public class VideoFileAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    public static ArrayList<VideoFile> videoFiles;
    private final Context mContext;
    public View view;

    public VideoFileAdapter(Context mContext, ArrayList<VideoFile> videoFiles) {
        this.mContext = mContext;
        VideoFileAdapter.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_file_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.fileName.setText(videoFiles.get(position).getTitle());
        Glide.with(mContext)
                .load(new File(videoFiles.get(position).getPath()))
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayer.class);
            intent.putExtra("position", position);
            intent.putExtra("sender", "FilesIsSending");
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return videoFiles.size();
    }
}

package com.example.layout_test.ui.videos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.layout_test.R;

import java.io.File;
import java.util.ArrayList;

public class VideoFolderAdapter2 extends RecyclerView.Adapter<VideoFolderAdapter2.MyViewHolder> {
    private Context mContext;
    static ArrayList<VideoFile> folderVideoFiles;
    View view;

    public VideoFolderAdapter2(Context mContext, ArrayList<VideoFile> folderVideoFiles) {
        this.mContext = mContext;
        VideoFolderAdapter2.folderVideoFiles = folderVideoFiles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.video_file_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.fileName.setText(folderVideoFiles.get(position).getTitle());
        Glide.with(mContext)
                .load(new File(folderVideoFiles.get(position).getPath()))
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoPlayer.class);
            intent.putExtra("position", position);
            intent.putExtra("sender", "FolderIsSending");
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return folderVideoFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail, menuMore;
        TextView fileName, videoDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
            menuMore = itemView.findViewById(R.id.video_menu_more);
            fileName = itemView.findViewById(R.id.video_file_name);
            videoDuration = itemView.findViewById(R.id.video_duration);
        }
    }
}

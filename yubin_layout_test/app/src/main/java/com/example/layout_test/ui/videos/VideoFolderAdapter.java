package com.example.layout_test.ui.videos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.R;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<String> folderNames;
    private ArrayList<VideoFile> videoFiles;

    public VideoFolderAdapter(Context mContext, ArrayList<String> folderNames, ArrayList<VideoFile> videoFiles) {
        this.mContext = mContext;
        this.folderNames = folderNames;
        this.videoFiles = videoFiles;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_folder_item, parent,
                false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        int index = folderNames.get(position).lastIndexOf("/");
        String folder = folderNames.get(position).substring(index + 1);
        holder.folder.setText(folder);
        holder.counterFile.setText(String.valueOf(numberOfFiles(folderNames.get(position))));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, VideoFolderActivity.class);
            intent.putExtra("folderName", folderNames.get(position));
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        TextView folder, counterFile;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            folder = itemView.findViewById(R.id.folderName);
            counterFile = itemView.findViewById(R.id.countFilesFolder);
        }
    }

    int numberOfFiles(String folderName) {
        int countFiles = 0;
        for (VideoFile videoFile : videoFiles) {
            if (videoFile.getPath().substring(0, videoFile.getPath().lastIndexOf("/"))
                    .endsWith(folderName)) {
                countFiles++;
            }
        }
        return countFiles;
    }

}

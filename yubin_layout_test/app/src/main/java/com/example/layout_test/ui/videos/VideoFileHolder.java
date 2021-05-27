package com.example.layout_test.ui.videos;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.R;

public class VideoFileHolder extends RecyclerView.ViewHolder {
    ImageView thumbnail, menuMore;
    TextView fileName, videoDuration;

    public VideoFileHolder(@NonNull View itemView) {
        super(itemView);
        thumbnail = itemView.findViewById(R.id.video_thumbnail);
        menuMore = itemView.findViewById(R.id.video_menu_more);
        fileName = itemView.findViewById(R.id.video_file_name);
        videoDuration = itemView.findViewById(R.id.video_duration);
    }
}

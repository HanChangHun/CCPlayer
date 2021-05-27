package com.example.layout_test.ui.videos;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layout_test.R;

class VideoFolderHolder extends RecyclerView.ViewHolder {
    TextView folder, counterFile;

    public VideoFolderHolder(@NonNull View itemView) {
        super(itemView);
        folder = itemView.findViewById(R.id.folderName);
        counterFile = itemView.findViewById(R.id.countFilesFolder);
    }
}

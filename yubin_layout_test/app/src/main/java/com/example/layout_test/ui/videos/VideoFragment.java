package com.example.layout_test.ui.videos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.layout_test.R;

import org.jetbrains.annotations.NotNull;

import static androidx.navigation.fragment.NavHostFragment.findNavController;

public class VideoFragment extends Fragment {
    View view;
    static int viewType = 0;  // 0: file view, 1: folder view

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (viewType == 0) {
            findNavController(this).navigate(R.id.action_navigation_video_to_navigation_video_file);
        } else if (viewType == 1) {
            findNavController(this).navigate(R.id.action_navigation_video_to_navigation_video_folder);
        } else {
            view = inflater.inflate(R.layout.screen_video, container, false);
        }
        return view;
    }
}

package com.example.layout_test.ui.videos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.layout_test.R;

import java.util.Objects;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.example.layout_test.MainActivity.videoFiles;

public class VideoFileFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    VideoFileAdapter videoFileAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.screen_video_file, container, false);
        recyclerView = view.findViewById(R.id.filesRV);
        if (videoFiles != null && videoFiles.size() > 0) {
            videoFileAdapter = new VideoFileAdapter(getContext(), videoFiles);
            recyclerView.setAdapter(videoFileAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_videos, menu);
        MenuItem item = menu.findItem(R.id.action_video_view_type);
        item.setTitle(getString(R.string.menu_video_folder_view));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_video_view_type) {
            setFolderView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFolderView() {
        VideoFragment.viewType = 1;
        findNavController(this).navigate(R.id.action_navigation_video_file_to_navigation_video);
    }
}
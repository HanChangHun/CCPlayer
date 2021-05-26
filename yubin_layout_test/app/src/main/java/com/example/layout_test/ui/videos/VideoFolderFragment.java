package com.example.layout_test.ui.videos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.layout_test.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;
import static com.example.layout_test.MainActivity.folderList;
import static com.example.layout_test.MainActivity.videoFiles;

public class VideoFolderFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    VideoFolderAdapter videoFolderAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.screen_video_folder, container, false);
        recyclerView = view.findViewById(R.id.folderRV);
        if (folderList != null && folderList.size() > 0 && videoFiles != null) {
            videoFolderAdapter = new VideoFolderAdapter(getContext(), folderList, videoFiles);
            recyclerView.setAdapter(videoFolderAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu_videos, menu);
        MenuItem item = menu.findItem(R.id.action_video_view_type);
        item.setTitle(getString(R.string.menu_video_file_view));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_video_view_type) {
            VideoFragment.viewType = 0;
            findNavController(this).navigate(R.id.action_navigation_video_folder_to_navigation_video);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
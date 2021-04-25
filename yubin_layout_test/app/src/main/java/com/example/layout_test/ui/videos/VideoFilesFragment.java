package com.example.layout_test.ui.videos;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.layout_test.R;

import static com.example.layout_test.MainActivity.videoFiles;

public class VideoFilesFragment extends Fragment {
    RecyclerView recyclerView;
    View view;
    VideoFilesAdapter videoFilesAdapter;

    public VideoFilesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.screen_video_files, container, false);
        recyclerView = view.findViewById(R.id.filesRV);
        if (videoFiles != null && videoFiles.size() > 0) {
            videoFilesAdapter = new VideoFilesAdapter(getContext(), videoFiles);
            recyclerView.setAdapter(videoFilesAdapter);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_video_view_type:
                setFolderView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setFolderView() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment, new VideoFolderFragment());
        fragmentTransaction.commitAllowingStateLoss();
    }

}
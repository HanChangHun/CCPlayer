package com.example.layout_test.ui.videos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.layout_test.R;

import java.util.ArrayList;

public class VideoFolderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    VideoFolderAdapter2 videoFolderAdapter2;
    String mFolderName;
    ArrayList<VideoFiles> videoFilesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folder);
        recyclerView = findViewById(R.id.folderVideoRV);
        mFolderName = getIntent().getStringExtra("folderName");
        if (mFolderName != null) videoFilesArrayList = getAllVideos(this, mFolderName);
        if (videoFilesArrayList.size() > 0) {
            videoFolderAdapter2 = new VideoFolderAdapter2(this, videoFilesArrayList);
            recyclerView.setAdapter(videoFolderAdapter2);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    RecyclerView.VERTICAL, false));
        }
    }

    public ArrayList<VideoFiles> getAllVideos(Context context, String folderName) {
        ArrayList<VideoFiles> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        };

        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderName + "%"};

        Cursor cursor = context.getContentResolver().query(uri, projection,
                selection, selectionArgs, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                String bucket_name = cursor.getString(7);
                VideoFiles videoFiles = new VideoFiles(id, path, title, fileName, size,
                        dateAdded, duration);
                Log.e("Path", path);  // 파일 존재 확인

                if (folderName.endsWith(bucket_name)) tempVideoFiles.add(videoFiles);

                tempVideoFiles.add(videoFiles);
            }
            cursor.close();
        }
        return tempVideoFiles;
    }
}
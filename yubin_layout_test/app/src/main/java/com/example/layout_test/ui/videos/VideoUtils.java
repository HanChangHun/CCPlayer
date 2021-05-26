package com.example.layout_test.ui.videos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

import static com.example.layout_test.MainActivity.folderList;

public class VideoUtils {

    public static ArrayList<VideoFile> getAllVideos(Context context) {
        ArrayList<VideoFile> tempVideoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                VideoFile videoFile = new VideoFile(id, path, title, fileName, size,
                        dateAdded, duration);
                Log.e("201521037", path);  // 파일 존재 확인

                // get file name in folder view
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0, slashFirstIndex);

                // add all folder name
                if (!folderList.contains(subString))
                    folderList.add(subString);


                tempVideoFiles.add(videoFile);
            }
            cursor.close();
        }
        return tempVideoFiles;
    }
}

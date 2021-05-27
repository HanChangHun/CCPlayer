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
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DISPLAY_NAME
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String path = cursor.getString(pathColumn);
                String title = cursor.getString(titleColumn);
                int duration = cursor.getInt(durationColumn);
                String fileName = cursor.getString(nameColumn);
                VideoFile videoFile = new VideoFile(id, path, title, fileName, duration);
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

    public static void getAllVideos2(Context context) {
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.Media.DATA,
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                String path = cursor.getString(column_index);
                Log.e("201521037", path);  // 파일 존재 확인
            }
            cursor.close();
        }
    }
}

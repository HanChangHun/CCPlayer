package com.example.layout_test.ui.videos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;


import java.util.ArrayList;

import static com.example.layout_test.MainActivity.folderList;

public class VideoUtils {
    public static ArrayList<VideoFile> getAllVideos(Context context, String folderName) {
        String TAG = "201521037";
        ArrayList<VideoFile> tempVideoFiles = new ArrayList<>();
        Cursor cursor;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        @SuppressLint("InlinedApi") String[] projection = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.TITLE
        };
        if (folderName == null) {
            cursor = context.getContentResolver().query(uri, projection,
                    null, null, null);
        } else {
            String selection = MediaStore.Video.Media.DATA + " like?";
            String[] selectionArgs = new String[]{"%" + folderName + "%"};
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
        }
        if (cursor != null) {
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            @SuppressLint("InlinedApi") int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String path = cursor.getString(pathColumn);
                String title = cursor.getString(titleColumn);
                int duration = cursor.getInt(durationColumn);
                String fileName = cursor.getString(nameColumn);

                Log.d(TAG, "getAllVideos: path: " + path);  // 파일 존재 확인

                VideoFile videoFile = new VideoFile(id, path, title, fileName, duration);
                tempVideoFiles.add(videoFile);

                if (folderName == null) {
                    // get file name in folder view
                    int slashFirstIndex = path.lastIndexOf("/");
                    String subString = path.substring(0, slashFirstIndex);

                    // add all folder name
                    if (!folderList.contains(subString))
                        folderList.add(subString);
                }
            }
            cursor.close();
        }
        return tempVideoFiles;
    }

    public static String timeFormat(int time) {
        time /= 1000;
        int minutes = time / 60;
        int seconds = time % 60;

        String strTemp;
        if (minutes < 10) strTemp = "0" + minutes + ":";
        else strTemp = minutes + ":";

        if (seconds < 10) strTemp += "0" + seconds;
        else strTemp += seconds;

        return strTemp;
    }
}

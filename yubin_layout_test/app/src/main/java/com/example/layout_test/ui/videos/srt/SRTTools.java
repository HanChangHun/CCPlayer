package com.example.layout_test.ui.videos.srt;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SRTTools {
    private final static String EXPRESSION1 = "[0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9] --> [0-9][0-9]:[0-5][0-9]:[0-5][0-9],[0-9][0-9][0-9]";

    private ArrayList<SRT> srts;

    public ArrayList<SRT> getSrts() {
        return this.srts;
    }

    public SRTTools() {
        srts = new ArrayList<>();
    }

    public void parseSRT(String filepath) {
        String charset = getCharset(filepath);
        Log.d("201521037", "parseSRT: charset: " + charset);
        String line = null;
        String startTime, endTime;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(filepath)), charset));
            while ((line = reader.readLine()) != null) {
                Log.d("201521037", "parseSRT: line: " + line);
                if (!line.equals("")) {
                    if (Pattern.matches(EXPRESSION1, line)) {
                        Log.d("201521037", "parseSRT: matched!!");
                        startTime = line.substring(0, 12);
                        endTime = line.substring(17, 29);
                        long start = TimeToMs(startTime);
                        long end = TimeToMs(endTime);
                        srts.add(new SRT(start, end));
                    } else {
                        Log.d("201521037", "parseSRT: expression not match...");
                    }
                } else {
                    Log.d("201521037", "parseSRT: line == \"\"");
                }
            }
            reader.close();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public long getPrev(long time) {
        long prevBeginTime = 0;
        for (SRT srt : srts) {
            if (srt.getEndTime() >= time) {
                if ((srt.getBeginTime() - time) <= 500) {
                    return prevBeginTime;
                } else {
                    return srt.getBeginTime();
                }
            } else {
                prevBeginTime = srt.getBeginTime();
            }
        }
        return 0;
    }

    public long getNext(long time) {
        boolean flag = false;
        for (SRT srt : srts) {
            if (flag) return srt.getBeginTime();
            if (srt.getEndTime() >= time) flag = true;
        }
        return 0;
    }

    private static long TimeToMs(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        int second = Integer.parseInt(time.substring(6, 8));
        int milli = Integer.parseInt(time.substring(9, 12));
        return (hour * 3600 + minute * 60 + second) * 1000 + milli;
    }

    public static String getCharset(String fileName) {
        String code = "UTF-8";
        try {
            BufferedInputStream bin = new BufferedInputStream(
                    new FileInputStream(fileName));
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}

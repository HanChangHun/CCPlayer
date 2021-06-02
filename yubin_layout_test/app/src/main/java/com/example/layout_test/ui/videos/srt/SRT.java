package com.example.layout_test.ui.videos.srt;

public class SRT {
    private long beginTime;
    private long endTime;

    public SRT(long beginTime, long endTime) {
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

package com.example.layout_test.ui.videos;

public class VideoFile {
    private long id;
    private String path;
    private String title;
    private String fileName;
    private int duration;
    private String subtitlePath;

    public VideoFile(long id, String path, String title,
                     String fileName, int duration) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.fileName = fileName;
        this.duration = duration;
        this.subtitlePath = path.replace(".mkv", ".srt").replace(".mp4", ".srt");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

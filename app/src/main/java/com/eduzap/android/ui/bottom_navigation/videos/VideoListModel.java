package com.eduzap.android.ui.bottom_navigation.videos;

public class VideoListModel {
    private String videoName, videoDescription, videoUrl, videoThumbnail;

    public VideoListModel() {
    }

    public VideoListModel(String videoName, String videoDescription, String videoUrl, String videoThumbnail) {
        this.videoName = videoName;
        this.videoDescription = videoDescription;
        this.videoUrl = videoUrl;
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
}

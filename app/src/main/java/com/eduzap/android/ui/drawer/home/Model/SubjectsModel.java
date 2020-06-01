package com.eduzap.android.ui.drawer.home.Model;

import com.eduzap.android.ui.bottom_navigation.videos.VideoListModel;

import java.util.ArrayList;

public class SubjectsModel {
    private String name, image;

    public SubjectsModel() {
    }

    public SubjectsModel(String name, String image, ArrayList<VideoListModel> videoItem) {
        this.name = name;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

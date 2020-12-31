package com.eduzap.android.ui.drawer.home.Model;

import androidx.annotation.Keep;

@Keep
public class SliderModel {
    private String description;
    private String imageUrl;

    public SliderModel(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public SliderModel() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

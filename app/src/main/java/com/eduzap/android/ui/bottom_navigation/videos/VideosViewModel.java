package com.eduzap.android.ui.bottom_navigation.videos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VideosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VideosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is video fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
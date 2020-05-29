package com.eduzap.android.ui.drawer.downloads;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DownloadsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DownloadsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment to show list of downloaded documents.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
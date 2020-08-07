package com.eduzap.android.ui.drawer.team;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeamViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TeamViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is contact us fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

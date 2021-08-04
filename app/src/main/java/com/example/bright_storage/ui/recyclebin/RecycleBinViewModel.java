package com.example.bright_storage.ui.recyclebin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecycleBinViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RecycleBinViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("物品");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
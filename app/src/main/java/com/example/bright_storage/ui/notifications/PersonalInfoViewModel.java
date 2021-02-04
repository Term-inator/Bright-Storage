package com.example.bright_storage.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalInfoViewModel extends ViewModel
{

    private MutableLiveData<String> mText;

    public PersonalInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("我");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
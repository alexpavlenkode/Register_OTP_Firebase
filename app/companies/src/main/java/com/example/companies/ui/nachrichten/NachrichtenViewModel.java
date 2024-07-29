package com.example.companies.ui.nachrichten;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NachrichtenViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NachrichtenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
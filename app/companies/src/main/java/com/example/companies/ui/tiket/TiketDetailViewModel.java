package com.example.companies.ui.tiket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TiketDetailViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TiketDetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Task Full fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

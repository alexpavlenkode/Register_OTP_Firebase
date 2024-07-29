package com.example.companies.ui.meldungen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeldungenViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MeldungenViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Meldungen fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.companies.ui.task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TaskDetailViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TaskDetailViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Task Full fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

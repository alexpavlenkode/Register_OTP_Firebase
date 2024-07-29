package com.example.companies.ui.profile;

        import androidx.lifecycle.LiveData;
        import androidx.lifecycle.MutableLiveData;
        import androidx.lifecycle.ViewModel;

public class ProfileVilewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileVilewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Profile fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.registerotp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginModell implements Parcelable {
    protected LoginModell(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoginModell> CREATOR = new Creator<LoginModell>() {
        @Override
        public LoginModell createFromParcel(Parcel in) {
            return new LoginModell(in);
        }

        @Override
        public LoginModell[] newArray(int size) {
            return new LoginModell[size];
        }
    };
}

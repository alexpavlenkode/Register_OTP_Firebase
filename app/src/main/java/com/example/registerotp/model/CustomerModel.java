package com.example.registerotp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class CustomerModel implements Parcelable {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String userId;
    private String password;

    public CustomerModel() {
    }

    public CustomerModel(String phone, String username, Timestamp createdTimestamp, String userId, String password) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.password = password;
    }

    protected CustomerModel(Parcel in) {
        phone = in.readString();
        username = in.readString();
        createdTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
        password = in.readString();
    }

    public static final Creator<CustomerModel> CREATOR = new Creator<CustomerModel>() {
        @Override
        public CustomerModel createFromParcel(Parcel in) {
            return new CustomerModel(in);
        }

        @Override
        public CustomerModel[] newArray(int size) {
            return new CustomerModel[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(username);
        dest.writeParcelable(createdTimestamp, flags);
        dest.writeString(userId);
        dest.writeString(password);
    }
}

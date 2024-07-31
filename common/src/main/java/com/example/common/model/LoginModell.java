package com.example.registerotp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class LoginModell implements Parcelable {
    private Timestamp createdTimestamp;
    private String userId;
    private boolean isPrivatPerson;
    private boolean regComplet;

    public LoginModell() {
    }

    public LoginModell(Timestamp createdTimestamp, String userId, boolean isPrivatPerson, boolean regComplet){
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.isPrivatPerson = isPrivatPerson;
        this.regComplet = regComplet;
    }

    protected LoginModell(Parcel in) {
        createdTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
        isPrivatPerson = in.readByte() != 0;
        regComplet = in.readByte() != 0;
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

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPrivatPerson(boolean privatPerson) {
        isPrivatPerson = privatPerson;
    }

    public void setRegComplet(boolean regComplet) {
        this.regComplet = regComplet;
    }
    public Timestamp getCreatedTimestamp(){
        return createdTimestamp;
    }
    public String getUserId(){
        return userId;
    }
    public Boolean getisPrivatPerson(){
        return isPrivatPerson;
    }
    public Boolean getRegComplet(){
        return regComplet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        //dest.writeParcelable(createdTimestamp, flags);
        dest.writeString(userId);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(regComplet);
        }*/

    }
}

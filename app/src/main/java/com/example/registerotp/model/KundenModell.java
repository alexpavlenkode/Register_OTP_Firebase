package com.example.registerotp.model;
/**
 * Die KundenModell-Klasse repräsentiert die Daten eines Benutzers in der Android-Anwendung.
 * Sie enthält Informationen wie Telefonnummer, Benutzername, Erstellungszeitstempel,
 * Benutzer-ID und Passwort. Diese Klasse wurde entwickelt, um die Verwaltung und den Austausch
 * von Benutzerdaten innerhalb der Anwendung zu erleichtern.
 */
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.Timestamp;

public class KundenModell implements Parcelable {
    private String phone;
    private String user;
    private String loginname;
    private Timestamp createdTimestamp;
    private String userId;
    private String password;
    private String email;
    private boolean regComplet;

    public KundenModell() {
    }

    public KundenModell(String phone, String user, String loginname, String email, Timestamp createdTimestamp, String userId, boolean regComplet) {
        this.phone = phone;
        this.user = user;
        this.loginname = loginname;
        this.email = email;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.regComplet = regComplet;
    }



    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected KundenModell(Parcel in) {
        phone = in.readString();
        user = in.readString();
        loginname = in.readString();
        email = in.readString();
        createdTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        userId = in.readString();
        regComplet = in.readBoolean();
    }

    public static final Creator<KundenModell> CREATOR = new Creator<KundenModell>() {
        @Override
        public KundenModell createFromParcel(Parcel in) {
            return new KundenModell(in);
        }

        @Override
        public KundenModell[] newArray(int size) {
            return new KundenModell[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return user;
    }

    public void setUsername(String user) {
        this.user = user;
    }
    public String getLoginName() {
        return loginname;
    }
    public void setLoginName(String loginname) {
        this.loginname = loginname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String password) {
        this.email = email;
    }

    public boolean getRegComplet() {
        return regComplet;
    }

    public void setRegComplet(boolean regComplet) {
        this.regComplet = regComplet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(user);
        dest.writeString(loginname);
        dest.writeString(email);
        dest.writeParcelable(createdTimestamp, flags);
        dest.writeString(userId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dest.writeBoolean(regComplet);
        }
    }
}

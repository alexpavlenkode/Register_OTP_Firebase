package com.example.companies.adapter;

public class Auftraggeber {
    private String userId;
    private String userName;
    private String userImage;
    private boolean isOnline;

    public Auftraggeber(String userId,
                        String userName,
                        String userImage,
                        boolean isOnline) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.isOnline = isOnline;
    }

    // Пустой конструктор необходим для Firestore
    public Auftraggeber() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}

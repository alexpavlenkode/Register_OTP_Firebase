package com.example.companies.adapter;

public class User {
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_COMPLETED = 2;

    private String photoUrl;
    private boolean isOnline;
    private String name;
    private String lastMessage;
    private String messageTime;
    private int unreadCount;
    private int status;
    private String userId;

    // Конструкторы, геттеры и сеттеры



    public User(String photoUrl, boolean isOnline, String name, String lastMessage, String messageTime, int unreadCount, int status, String userId) {
        this.photoUrl = photoUrl;
        this.isOnline = isOnline;
        this.name = name;
        this.lastMessage = lastMessage;
        this.messageTime = messageTime;
        this.unreadCount = unreadCount;
        this.status = status;
        this.userId = userId;
    }

    public String getPhotoUrl() { return photoUrl; }
    public boolean isOnline() { return isOnline; }
    public String getName() { return name; }
    public String getLastMessage() { return lastMessage; }
    public String getMessageTime() { return messageTime; }
    public int getUnreadCount() { return unreadCount; }
    public int getStatus() { return status; }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}


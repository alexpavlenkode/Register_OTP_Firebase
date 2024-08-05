package com.example.companies.ui.chat;

import android.util.Log;

import com.google.firebase.Timestamp;

import java.util.Date;

public class NachrichtenModel {
    private String chatroomId;
    private String userId;
    private String ticketId;
    private String userName;
    private String userImage;
    private String lastMessage;
    private Timestamp messageTimestamp;
    private String unreadMessages;
    private int ticketStatus;

    // Пустой конструктор необходим для Firestore
    public NachrichtenModel() {
    }



    public NachrichtenModel(String chatroomId,
                            String userId,
                            String userName,
                            String userImage,
                            String lastMessage,
                            Timestamp messageTimestamp,
                            String unreadMessages,
                            String ticketId,
                            int ticketStatus) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.lastMessage = lastMessage;
        this.messageTimestamp = messageTimestamp;
        this.unreadMessages = unreadMessages;
        this.ticketStatus = ticketStatus;
        this.ticketId = ticketId;
    }
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getMessageTimestamp() {
        return calculateTimeAgo(messageTimestamp);
    }

    public void setMessageTimestamp(Timestamp messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(String unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public static String calculateTimeAgo(Timestamp timestamp) {

        Date date = convertTimestampToDate(timestamp);
        Log.d("TiketDate", "Timestamp " + timestamp);
        Log.d("TiketDate", "Date " + date);
        if (date == null) {
            return "Unbekannt"; // "Unknown" or some placeholder
        }
        long time = date.getTime();
        long now = System.currentTimeMillis();
        long diff = now - time;

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return "Vor " + days + (days == 1 ? " Tag" : " Tagen"); // "1 day ago" or "X days ago"
        } else if (hours > 0) {
            return "Vor " + hours + (hours == 1 ? " Stunde" : " Stunden"); // "1 hour ago" or "X hours ago"
        } else if (minutes > 0) {
            return "Vor " + minutes + (minutes == 1 ? " Minute" : " Minuten"); // "1 minute ago" or "X minutes ago"
        } else {
            return "Jetzt"; // "Just now"
        }
    }
    // Метод для конвертации Timestamp в Date
    private static Date convertTimestampToDate(Timestamp timestamp) {
        return timestamp != null ? timestamp.toDate() : null;
    }
}

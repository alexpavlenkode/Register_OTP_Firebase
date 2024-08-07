package com.example.companies.ui.chat;

import com.google.firebase.Timestamp;

public class ChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;
    private boolean isSystemMessage;

    public ChatMessageModel() {
        // Пустой конструктор для Firestore
    }



    public ChatMessageModel(String message, String senderId, Timestamp timestamp, boolean isSystemMessage) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.isSystemMessage = isSystemMessage;
    }

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

    public void setSystemMessage(boolean systemMessage) {
        isSystemMessage = systemMessage;
    }

    // Геттеры и сеттеры
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}


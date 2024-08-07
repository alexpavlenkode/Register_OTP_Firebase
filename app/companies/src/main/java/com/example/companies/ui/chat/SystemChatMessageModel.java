package com.example.companies.ui.chat;

import com.google.firebase.Timestamp;

import java.util.List;

public class SystemChatMessageModel {
    private String message;
    private String senderId;
    private Timestamp timestamp;
    private boolean isTyping;
    private List<String> buttons;

    public SystemChatMessageModel() {
        // Пустой конструктор для Firestore
    }

    public SystemChatMessageModel(String message, String senderId, Timestamp timestamp,List<String> buttons) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.buttons = buttons;
    }

    public List<String> getButtons() {
        return buttons;
    }

    public void setButtons(List<String> buttons) {
        this.buttons = buttons;
    }

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
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
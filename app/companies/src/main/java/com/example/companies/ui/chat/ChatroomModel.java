package com.example.companies.ui.chat;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatroomModel {
    private String chatroomId;
    private String ticketId;
    private String userId;
    private String companyId;
    private Timestamp timestamp;

    // Пустой конструктор необходим для Firestore
    public ChatroomModel() {
    }



    public ChatroomModel(String chatroomId, String ticketId, String userId, String companyId, Timestamp timestamp) {
        this.chatroomId = chatroomId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.companyId = companyId;
        this.timestamp = timestamp;
    }

    // Геттеры и сеттеры
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
    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}


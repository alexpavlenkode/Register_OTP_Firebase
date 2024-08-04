package com.example.companies.ui.chat;
import com.example.common.model.FirmenModel;
public class PendingChatCreation {
    private String ticketId;
    private FirmenModel profile;

    public PendingChatCreation(String ticketId, FirmenModel profile) {
        this.ticketId = ticketId;
        this.profile = profile;
    }

    public String getTicketId() {
        return ticketId;
    }

    public FirmenModel getProfile() {
        return profile;
    }


}

package org.example.Bot;

import java.time.LocalDateTime;

public class UserSession {
    private String lastMessage;
    private LocalDateTime lastInteraction;

    public UserSession() {
        this.lastInteraction = LocalDateTime.now();
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        this.lastInteraction = LocalDateTime.now();
    }

    public LocalDateTime getLastInteraction() {
        return lastInteraction;
    }
}

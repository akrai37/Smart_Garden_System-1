package com.example.ooad_project.Events;

public class RainEvent {
    private final int precipitationAmount;

    public RainEvent(int amount) {
        this.precipitationAmount = amount;
    }

    public int getAmount() {
        return precipitationAmount;
    }
}

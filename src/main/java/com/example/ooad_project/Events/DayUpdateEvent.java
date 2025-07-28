package com.example.ooad_project.Events;

public class DayUpdateEvent {

    private final int currentDayNumber;

    public DayUpdateEvent(int dayNumber) {
        this.currentDayNumber = dayNumber;
    }

    public int getDay() {
        return currentDayNumber;
    }

}

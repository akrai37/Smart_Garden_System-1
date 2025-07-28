package com.example.ooad_project.Events;

public class TemperatureEvent {
        private final int temperatureValue;
        public TemperatureEvent(int amount) {
            this.temperatureValue = amount;
        }
        public int getAmount() {
            return temperatureValue;
        }
}
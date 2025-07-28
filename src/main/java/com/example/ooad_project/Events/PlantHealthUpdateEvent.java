package com.example.ooad_project.Events;

public class PlantHealthUpdateEvent {

        int plantRowIndex;
        int plantColumnIndex;
        int previousHealthLevel;
        int currentHealthLevel;

        public PlantHealthUpdateEvent(int row, int col, int oldHealth, int newHealth) {
            this.plantRowIndex = row;
            this.plantColumnIndex = col;
            this.previousHealthLevel = oldHealth;
            this.currentHealthLevel = newHealth;
        }

        public int getRow() {
            return plantRowIndex;
        }

        public int getCol() {
            return plantColumnIndex;
        }

        public int getOldHealth() {
            return previousHealthLevel;
        }

        public int getNewHealth() {
            return currentHealthLevel;
        }

}

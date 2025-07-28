package com.example.ooad_project.Events;

    public class SprinklerEvent {


    int requiredWaterAmount;

    int gridRowPosition;
    int gridColumnPosition;

    public SprinklerEvent(int row, int col, int waterNeeded) {
        this.gridRowPosition = row;
        this.gridColumnPosition = col;
        this.requiredWaterAmount = waterNeeded;
    }

    public int getRow() {
        return gridRowPosition;
    }

    public int getCol() {
        return gridColumnPosition;
    }

    public int getWaterNeeded() {
        return requiredWaterAmount;
    }


}

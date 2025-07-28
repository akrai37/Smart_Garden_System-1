package com.example.ooad_project.Events;

public class HeatTemperatureEvent {

    int heatingAmount;
    int plantGridRow;
    int plantGridColumn;



    public HeatTemperatureEvent(int row, int col, int tempDiff) {

        this.plantGridRow = row;
        this.plantGridColumn = col;
        this.heatingAmount = tempDiff;
    }

    public int getRow() {
        return plantGridRow;
    }

    public int getCol() {
        return plantGridColumn;
    }

    public int getTempDiff() {
        return heatingAmount;
    }
}

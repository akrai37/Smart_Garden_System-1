package com.example.ooad_project.Events;

public class CoolTemperatureEvent {

    int temperatureDelta;
    int plantRowPosition;
    int plantColumnPosition;


    public CoolTemperatureEvent(int plantRowPosition, int plantColumnPosition, int temperatureDelta) {

        this.plantRowPosition = plantRowPosition;
        this.plantColumnPosition = plantColumnPosition;
        this.temperatureDelta = temperatureDelta;
    }

    public int getRow() {
        return plantRowPosition;
    }

    public int getCol() {
        return plantColumnPosition;
    }

    public int getTempDiff() {
        return temperatureDelta;
    }


}

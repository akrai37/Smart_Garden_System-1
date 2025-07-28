package com.example.ooad_project.Events;

public class ParasiteDamageEvent {

    int plantRowPosition;
    int plantColumnPosition;
    int parasiteDamageAmount;

    public ParasiteDamageEvent(int row, int col, int damage) {
        this.plantRowPosition = row;
        this.plantColumnPosition = col;
        this.parasiteDamageAmount = damage;
    }

    public int getRow() {
        return plantRowPosition;
    }

    public int getCol() {
        return plantColumnPosition;
    }

    public int getDamage() {
        return parasiteDamageAmount;
    }


}

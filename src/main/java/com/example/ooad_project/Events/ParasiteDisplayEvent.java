package com.example.ooad_project.Events;

import com.example.ooad_project.Parasite.Parasite;

public class ParasiteDisplayEvent {

    private final Parasite detectedParasite;
    private final int gardenRowLocation;
    private final int gardenColumnLocation;


    public ParasiteDisplayEvent(Parasite parasite, int row, int column) {
        this.detectedParasite = parasite;
        this.gardenRowLocation = row;
        this.gardenColumnLocation = column;
    }

    public Parasite getParasite() {
        return detectedParasite;
    }

    public int getRow() {
        return gardenRowLocation;
    }

    public int getColumn() {
        return gardenColumnLocation;
    }

}

package com.example.ooad_project.Events;

import com.example.ooad_project.Plant.Plant;

public class PlantImageUpdateEvent {

    private final Plant affectedPlant;

    public PlantImageUpdateEvent(Plant plant) {
        this.affectedPlant = plant;
    }

    public Plant getPlant() {
        return affectedPlant;
    }


}

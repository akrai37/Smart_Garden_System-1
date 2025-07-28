package com.example.ooad_project.SubSystems;

import com.example.ooad_project.Events.DayUpdateEvent;
import com.example.ooad_project.GardenGrid;
import com.example.ooad_project.Plant.Plant;
import com.example.ooad_project.ThreadUtils.EventBus;
import com.example.ooad_project.Events.RainEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.example.ooad_project.Events.SprinklerEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class WateringSystem implements Runnable {
    private final AtomicBoolean isRainPending = new AtomicBoolean(false);
    private static final Logger wateringLogger = LogManager.getLogger("WateringSystemLogger");
    private int precipitationVolume = 0;
    private final GardenGrid plantGarden;
    private int simulationDay;
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Wait a bit before checking again
            } catch (InterruptedException e) {
                wateringLogger.error("Watering System interrupted");
                return; // Stop everything if something goes wrong
            }
        }
    }

    public WateringSystem() {
        wateringLogger.info("Watering System Initialized");
        // We watch for when it starts raining
        EventBus.subscribe("RainEvent", event -> handleRain((RainEvent) event));
        EventBus.subscribe("SprinklerActivationEvent", event -> sprinkle());
        EventBus.subscribe("DayUpdateEvent", event -> handleDayChangeEvent((DayUpdateEvent) event));
        // Get access to our garden
        this.plantGarden = GardenGrid.getInstance();
    }

    private void handleDayChangeEvent(DayUpdateEvent event) {
        this.simulationDay = event.getDay(); // Remember what day it is
    }

    // Rain waters everything the same amount
    private void handleRain(RainEvent event) {

        for (int rowIndex = 0; rowIndex < plantGarden.getNumRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < plantGarden.getNumCols(); columnIndex++) {
                Plant plantInGrid = plantGarden.getPlant(rowIndex, columnIndex);
                if (plantInGrid != null) {
                    plantInGrid.addWater(event.getAmount());
                    wateringLogger.info("Day: " + simulationDay + " Watered {} at position ({}, {}) with {} water from rain", plantInGrid.getName(), rowIndex, columnIndex, event.getAmount());
                }
            }
        }

    }

    // Smart sprinklers water plants based on what they need
    private void sprinkle() {
        wateringLogger.info("Day: " + simulationDay + " Sprinklers activated!");
        int wateredPlantsCount = 0; // Let's count how many we help

        for (int rowIndex = 0; rowIndex < plantGarden.getNumRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < plantGarden.getNumCols(); columnIndex++) {
                Plant plantInGrid = plantGarden.getPlant(rowIndex, columnIndex);
                if (plantInGrid != null && !plantInGrid.getIsWatered()) {
                    int requiredWaterAmount = plantInGrid.getWaterRequirement() - plantInGrid.getCurrentWater();
                    if (requiredWaterAmount > 0) {

                        // Show the sprinkler working on screen

                        EventBus.publish("Day: " + simulationDay + " SprinklerEvent", new SprinklerEvent(plantInGrid.getRow(), plantInGrid.getCol(), requiredWaterAmount));


                        plantInGrid.addWater(requiredWaterAmount);
                        // Write down what we did
                        wateringLogger.info("Day: " + simulationDay + " Sprinkled {} at position ({}, {}) with {} water from sprinklers", plantInGrid.getName(), rowIndex, columnIndex, requiredWaterAmount);
                        wateredPlantsCount++;
                    }else {
                        wateringLogger.info("Day: " + simulationDay + " {} at position ({}, {}) does not need water", plantInGrid.getName(), rowIndex, columnIndex);
                    }
                }
            }
        }

        wateringLogger.info("Day: " + simulationDay + " In total Sprinkled {} plants", wateredPlantsCount);
    }
}



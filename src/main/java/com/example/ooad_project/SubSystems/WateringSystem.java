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
    private static final Logger logger = LogManager.getLogger("WateringSystemLogger");
    private final GardenGrid gardenGrid;
    private int currentDay;
    // Make this static and volatile to ensure all threads see the same rain status
    private static final AtomicBoolean isCurrentlyRaining = new AtomicBoolean(false);
    private volatile boolean isRunning = true; // Flag to control the run loop
    @Override
    public void run() {
        while (isRunning) {
            try {
                // Sleep for a random duration between 30s to 90s
                int delay = 30 + (int) (Math.random() * 61); // 30–90 sec
                Thread.sleep(delay * 1000L);

                if (!isRunning) break; // Check again after sleep

                // Random rain amount between 5mm to 30mm
                int rainAmount = 5 + (int) (Math.random() * 26); // 5–30
                EventBus.publish("RainEvent", new RainEvent(rainAmount));

                logger.info("Day: " + currentDay + " ⛈️ Triggered random rain of " + rainAmount + "mm after " + delay + " seconds");

            } catch (InterruptedException e) {
                logger.error("Watering System interrupted");
                isRunning = false;
                return;
            }
        }
        logger.info("Watering System stopped");
    }

    public void shutdown() {
        isRunning = false;
        isCurrentlyRaining.set(false); // Reset rain state
        logger.info("Watering System shutdown requested");
    }


    public WateringSystem() {
        logger.info("Watering System Initialized");
        // Reset rain state when creating new instance
        isCurrentlyRaining.set(false);
//        So our watering system is subscribed to the RainEvent
//        When a rain event is published, the watering system will handle it
        EventBus.subscribe("RainEvent", event -> handleRain((RainEvent) event));
        EventBus.subscribe("SprinklerActivationEvent", event -> sprinkle());
        EventBus.subscribe("DayUpdateEvent", event -> handleDayChangeEvent((DayUpdateEvent) event));
        //        Get the garden grid instance
//        This is the grid that holds all the plants
        this.gardenGrid = GardenGrid.getInstance();
    }

    // Static method to reset global rain state
    public static void resetRainState() {
        isCurrentlyRaining.set(false);
        LogManager.getLogger("WateringSystemLogger").info("Global rain state reset to NOT RAINING");
    }

    private void handleDayChangeEvent(DayUpdateEvent event) {
        this.currentDay = event.getDay(); // Update currentDay
    }

//    This method is called when a rain event is published
//    It waters all the plants in the garden grid
//    The amount of water each plant gets is the same
    private synchronized void handleRain(RainEvent event) {
        // Set rain state to true when rain starts
        isCurrentlyRaining.set(true);
        logger.info("Day: " + currentDay + " ☔ RAIN STARTED - Sprinklers will be disabled for the next 10 seconds");

        for (int i = 0; i < gardenGrid.getNumRows(); i++) {
            for (int j = 0; j < gardenGrid.getNumCols(); j++) {
                Plant plant = gardenGrid.getPlant(i, j);
                if (plant != null) {
                    plant.addWater(event.getAmount());
                    logger.info("Day: " + currentDay + " Watered {} at position ({}, {}) with {} water from rain", plant.getName(), i, j, event.getAmount());
                }
            }
        }

        // Schedule rain to stop after 10 seconds (extended duration to avoid conflicts)
        new Thread(() -> {
            try {
                Thread.sleep(10000); // 10 seconds - extended to avoid day cycle conflicts
                isCurrentlyRaining.set(false);
                logger.info("Day: " + currentDay + " ☀️ RAIN STOPPED - Sprinklers can now operate normally");
            } catch (InterruptedException e) {
                logger.error("Rain timer interrupted", e);
            }
        }).start();

    }



//    This method is called when the sprinklers are activated
//    It waters all the plants in the garden grid
//    The amount of water each plant gets depends on how much water it needs
    private synchronized void sprinkle() {
        // Check if it's currently raining before activating sprinklers
        boolean isRaining = isCurrentlyRaining.get();
        logger.info("Day: " + currentDay + " Sprinkler activation requested. Rain status: " + (isRaining ? "RAINING" : "NOT RAINING"));
        
        if (isRaining) {
            logger.info("Day: " + currentDay + " Sprinklers activation SKIPPED - Currently raining!");
            return;
        }

//        System.out.println("Sprinklers activated!");
        logger.info("Day: " + currentDay + " Sprinklers activated - Rain has stopped!");
        int counter = 0; // Counter to keep track of how many plants are watered

        for (int i = 0; i < gardenGrid.getNumRows(); i++) {
            for (int j = 0; j < gardenGrid.getNumCols(); j++) {
                Plant plant = gardenGrid.getPlant(i, j);
                if (plant != null && !plant.getIsWatered()) {
                    int waterNeeded = plant.getWaterRequirement() - plant.getCurrentWater();
                    if (waterNeeded > 0) {

//                        Publish water needed later

                        EventBus.publish("Day: " + currentDay + " SprinklerEvent", new SprinklerEvent(plant.getRow(), plant.getCol(), waterNeeded));


                        plant.addWater(waterNeeded);
//                        Want to specify that the water is from sprinklers
                        logger.info("Day: " + currentDay + " Sprinkled {} at position ({}, {}) with {} water from sprinklers", plant.getName(), i, j, waterNeeded);
                        counter++;
                    }else {
                        logger.info("Day: " + currentDay + " {} at position ({}, {}) does not need water", plant.getName(), i, j);
                    }
                }
            }
        }

        logger.info("Day: " + currentDay + " In total Sprinkled {} plants", counter);
//        gardenGrid.printAllPlantStats();
    }
}



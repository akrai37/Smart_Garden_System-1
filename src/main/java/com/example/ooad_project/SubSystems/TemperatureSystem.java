package com.example.ooad_project.SubSystems;

import com.example.ooad_project.Events.*;
import com.example.ooad_project.GardenGrid;
import com.example.ooad_project.Plant.Plant;
import com.example.ooad_project.ThreadUtils.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TemperatureSystem implements Runnable{
    private int simulationDayNumber;
    private final GardenGrid plantLayout;
    private static final Logger temperatureLogger = LogManager.getLogger("TemperatureSystemLogger");


    public TemperatureSystem() {
        // Set up our garden temperature controller
        // We listen for temperature changes from the weather API
        this.plantLayout = GardenGrid.getInstance();
        temperatureLogger.info("Temperature System Initialized");
        EventBus.subscribe("DayUpdateEvent", event -> handleDayChangeEvent((DayUpdateEvent) event));
        EventBus.subscribe("TemperatureEvent", event -> handleTemperatureEvent((TemperatureEvent) event));
    }

    private void handleDayChangeEvent(DayUpdateEvent event) {
        this.simulationDayNumber = event.getDay(); // Keep track of what day we're on
    }

    private void handleTemperatureEvent(TemperatureEvent event) {
        int environmentalTemperature = event.getAmount();
        temperatureLogger.info("Day: " + simulationDayNumber + " API called temperature set to: {}", environmentalTemperature);

        for (int rowPosition = 0; rowPosition < plantLayout.getNumRows(); rowPosition++) {
            for (int columnPosition = 0; columnPosition < plantLayout.getNumCols(); columnPosition++) {
                Plant gardenPlant = plantLayout.getPlant(rowPosition, columnPosition);
                if (gardenPlant != null) {
                    int temperatureDifference = environmentalTemperature - gardenPlant.getTemperatureRequirement();
                    if (temperatureDifference > 0) {
                        EventBus.publish("Day: " + simulationDayNumber + " CoolTemperatureEvent", new CoolTemperatureEvent(gardenPlant.getRow(), gardenPlant.getCol(), Math.abs(temperatureDifference)));
                        temperatureLogger.info("Day: " + simulationDayNumber + " Temperature system cooled {} at position ({}, {}) by {} degrees F.", gardenPlant.getName(), rowPosition, columnPosition, Math.abs(temperatureDifference));
                        EventBus.publish("SprinklerEvent", new SprinklerEvent(gardenPlant.getRow(), gardenPlant.getCol(), temperatureDifference));
                        temperatureLogger.info("Day: " + simulationDayNumber + " Sprinklers started at position ({}, {}) to cool down the plant.", rowPosition, columnPosition);
                    } else if (temperatureDifference < 0) {
                        EventBus.publish("HeatTemperatureEvent", new HeatTemperatureEvent(gardenPlant.getRow(), gardenPlant.getCol(), Math.abs(temperatureDifference)));
                        temperatureLogger.info("Day: " + simulationDayNumber + " Temperature system heated {} at position ({}, {}) by {} degrees F.", gardenPlant.getName(), rowPosition, columnPosition, Math.abs(temperatureDifference));
                    } else {
                        temperatureLogger.info("Day: " + simulationDayNumber + " {} at position ({}, {}) is at optimal temperature.", gardenPlant.getName(), rowPosition, columnPosition);
                    }
                }
            }
        }
    }


    public void run() {

        while (true) {
            try {
                temperatureLogger.info("Day: " + simulationDayNumber + " All Levels are optimal");
                Thread.sleep(20000);
                // Taking a break to check temperatures again
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}

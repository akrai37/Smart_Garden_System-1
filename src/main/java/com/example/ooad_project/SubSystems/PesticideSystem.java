package com.example.ooad_project.SubSystems;

import com.example.ooad_project.Events.DayUpdateEvent;
import com.example.ooad_project.Events.ParasiteDisplayEvent;
import com.example.ooad_project.Events.PesticideApplicationEvent;
import com.example.ooad_project.Events.ParasiteEvent;
import com.example.ooad_project.GardenGrid;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;
import com.example.ooad_project.ThreadUtils.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PesticideSystem implements Runnable{
    private int currentSimulationDay;
    private final GardenGrid gardenArea;
    private static final Logger systemLogger = LogManager.getLogger("PesticideSystemLogger");

    public PesticideSystem() {
        this.gardenArea = GardenGrid.getInstance();
        // Print message removed, using logger instead
        systemLogger.info("Pesticide System Initialized");

        EventBus.subscribe("DayUpdateEvent", event -> handleDayChangeEvent((DayUpdateEvent) event));
        // Listen for bug attacks from the main garden system
        EventBus.subscribe("ParasiteEvent", event -> handlePesticideEvent((ParasiteEvent) event));
    }

    private void handleDayChangeEvent(DayUpdateEvent dayUpdateEvent) {
        this.currentSimulationDay = dayUpdateEvent.getDay(); // Update current simulation day
    }

    private void handlePesticideEvent(ParasiteEvent parasiteDetectionEvent) {
        Parasite detectedParasite = parasiteDetectionEvent.getParasite();
        // Check every plant in our garden to see if this bug can hurt it
        for (int rowIndex = 0; rowIndex < gardenArea.getNumRows(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < gardenArea.getNumCols(); columnIndex++) {
                Plant currentPlant = gardenArea.getPlant(rowIndex, columnIndex);
                if (currentPlant != null && detectedParasite.getAffectedPlants().contains(currentPlant.getName())) {
                    // Show the bug on the plant first
                    EventBus.publish("DisplayParasiteEvent", new ParasiteDisplayEvent(detectedParasite, rowIndex, columnIndex));

                    // Let the bug damage the plant
                    detectedParasite.affectPlant(currentPlant);
                    systemLogger.info("Day: " + currentSimulationDay + " Pesticide system applied {} to {} at position ({}, {})",
                            detectedParasite.getName(), currentPlant.getName(), rowIndex, columnIndex);

                    // Spray pesticide to help the plant
                    EventBus.publish("PesticideApplicationEvent",
                            new PesticideApplicationEvent(rowIndex, columnIndex, "standard"));

                    // Help the plant recover from the attack
                    currentPlant.healPlant(detectedParasite.getDamage()/2);
                }
            }
        }
    }


//    We pause the system to give time for the UI to show the bug attack effects


    public void run() {
        while (true) {
            try {

//          Just keeps the thread running in the background

                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }



}

package com.example.ooad_project.API;

import com.example.ooad_project.DaySystem;
import com.example.ooad_project.Events.ParasiteEvent;
import com.example.ooad_project.Events.RainEvent;
import com.example.ooad_project.Events.TemperatureEvent;
import com.example.ooad_project.GardenGrid;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Parasite.ParasiteManager;
import com.example.ooad_project.Plant.Plant;
import com.example.ooad_project.Plant.PlantManager;
import com.example.ooad_project.ThreadUtils.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmartGardenAPI implements SmartGardenAPIInterface {
    private static final Logger apiLogger = LogManager.getLogger("GardenSimulationAPILogger");
    private ParasiteManager parasiteController = ParasiteManager.getInstance();

    @Override
    public void initializeGarden() {
        apiLogger.info("Initializing Garden");
        GardenGrid gardenGridInstance = GardenGrid.getInstance();
        PlantManager plantManagerInstance = PlantManager.getInstance();

        EventBus.publish("InitializeGarden", null);


//        }
    }


    @Override
    public Map<String, Object> getPlants() {
        try {
            apiLogger.info("API called to get plant information");

            List<String> allPlantNames = new ArrayList<>();
            List<Integer> allWaterRequirements = new ArrayList<>();
            List<List<String>> allParasiteVulnerabilities = new ArrayList<>();

            for (Plant currentPlant : GardenGrid.getInstance().getPlants()) {
                allPlantNames.add(currentPlant.getName());
                allWaterRequirements.add(currentPlant.getWaterRequirement());
                allParasiteVulnerabilities.add(currentPlant.getVulnerableTo());
            }

            Map<String, Object> plantDataResponse = Map.of(
                    "plants", allPlantNames,
                    "waterRequirement", allWaterRequirements,
                    "parasites", allParasiteVulnerabilities
            );

            System.out.println("\n\nResponse: from getPlants\n\n");
            System.out.println(plantDataResponse);

            return plantDataResponse;
        } catch (Exception exception) {
            apiLogger.error("Error occurred while retrieving plant information", exception);
            return null;
        }
    }

    @Override
    public void rain(int amount) {
        apiLogger.info("API called rain with amount: {}", amount);
        EventBus.publish("RainEvent", new RainEvent(amount));
    }

    @Override
    public void temperature(int amount) {
        apiLogger.info("API called temperature set to: {}", amount);
        EventBus.publish("TemperatureEvent", new TemperatureEvent(amount));
    }

    @Override
    public void parasite(String name) {
        apiLogger.info("API called to handle parasite: {}", name);
        Parasite parasiteInstance = parasiteController.getParasiteByName(name);
        if(parasiteInstance == null) {
            apiLogger.info("API - Parasite with name {} not found", name);
            return;
        }
        EventBus.publish("ParasiteEvent", new ParasiteEvent(parasiteInstance));

    }

    @Override
    public void getState() {
        apiLogger.info("Day: " + DaySystem.getInstance().getCurrentDay() + "API called to get current state of the garden.");
        StringBuilder gardenStateBuilder = new StringBuilder();
        gardenStateBuilder.append(String.format("Current Garden State as of Day %d:\n", DaySystem.getInstance().getCurrentDay()));

        GardenGrid currentGardenGrid = GardenGrid.getInstance();
        ArrayList<Plant> gardenPlants = currentGardenGrid.getPlants();

        if (gardenPlants.isEmpty()) {
            gardenStateBuilder.append("No plants are currently in the garden.\n");
        } else {
            for (Plant individualPlant : gardenPlants) {
                gardenStateBuilder.append(String.format("\nPlant Name: %s (Position: Row %d, Col %d)\n", individualPlant.getName(), individualPlant.getRow(), individualPlant.getCol()));
                gardenStateBuilder.append(String.format("  - Current Health: %d/%d\n", individualPlant.getCurrentHealth(), individualPlant.getHealthFull()));
                gardenStateBuilder.append(String.format("  - Growth Stage: %s\n", individualPlant.getGrowthStageDescription()));
                gardenStateBuilder.append(String.format("  - Water Status: %s (Current Water: %d, Requirement: %d)\n", individualPlant.getIsWatered() ? "Watered" : "Needs Water", individualPlant.getCurrentWater(), individualPlant.getWaterRequirement()));
                gardenStateBuilder.append(String.format("  - Temperature Requirement: %d degrees\n", individualPlant.getTemperatureRequirement()));
                gardenStateBuilder.append(String.format("  - Current Image: %s\n", individualPlant.getCurrentImage()));
                gardenStateBuilder.append(String.format("  - Vulnerable to: %s\n", String.join(", ", individualPlant.getVulnerableTo())));
            }
        }

        apiLogger.info(gardenStateBuilder.toString());
    }



}

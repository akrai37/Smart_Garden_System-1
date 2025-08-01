package com.example.ooad_project.Plant;


import com.example.ooad_project.Events.PlantHealthUpdateEvent;
import com.example.ooad_project.Events.PlantImageUpdateEvent;
import com.example.ooad_project.ThreadUtils.EventBus;
import java.lang.Math;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * This is what all plants are made from.
 * Every plant needs water and sun to grow.
 * All flowers, trees, and vegetables use this.
 */
public abstract class Plant {
    protected double health;
    protected final double maxHealth = 100.0;
    protected HealthBar healthBar;

    private final String botanicalName;
    private final int hydrationRequirement;
    private String currentImageFilePath;
    private Boolean isFullyHydrated = false;
    private int collectedMoisture = 0;
    private final int climateRequirement;
    private static final Logger logger = LogManager.getLogger("PesticideSystemLogger");
    private ArrayList<String> visualAssetLibrary;

    private final int baseHealthThreshold;
    private final int intermediateHealthThreshold;
    private final int peakHealthThreshold;
    private int vitality;

    private ArrayList<String> threatList;

    // Plant starts without a position until placed in garden
    private int gardenRowIndex = -1;
    private int gardenColumnIndex = -1;

    public Plant(String name, int waterRequirement, String imageName, int temperatureRequirement, ArrayList<String> vulnerableTo, int healthSmall, int healthMedium, int healthFull, ArrayList<String> allImages) {
        this.health = maxHealth;
        this.healthBar = new HealthBar(maxHealth);
        this.botanicalName = name;
        this.hydrationRequirement = waterRequirement;
        this.currentImageFilePath = imageName;
        this.climateRequirement = temperatureRequirement;
        this.threatList = vulnerableTo;
        this.baseHealthThreshold = healthSmall;
        this.intermediateHealthThreshold = healthMedium;
        this.peakHealthThreshold = healthFull;
        this.visualAssetLibrary = allImages;
        // New plants start small and grow over time
        this.vitality = healthSmall;
    }

    // Give water to help the plant grow

    public synchronized void addWater(int amount) {
        this.collectedMoisture = Math.min(collectedMoisture + amount, hydrationRequirement);
        this.isFullyHydrated = collectedMoisture >= hydrationRequirement;
    }

    public void takeDamage(double amount) {
        health = Math.max(0, health - amount);
        healthBar.updateHealth(health);
    }

    public void heal(double amount) {
        health = Math.min(maxHealth, health + amount);
        healthBar.updateHealth(health);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public double getHealth() {
        return health;
    }

    public synchronized void healPlant(int healAmount) {
        int previousStage = getHealthStage();
        // Help the plant recover but don't exceed maximum health
        this.vitality = Math.min(this.vitality + healAmount, this.peakHealthThreshold);

        // Check if plant grew to next stage
        int currentStage = getHealthStage();

        // Update plant appearance if it grew
        if (previousStage != currentStage) {
            updatePlantImage(currentStage);
            logger.info("Plant: {} at position ({}, {}) health stage changed to {}, updated image to {}",
                    this.botanicalName, this.gardenRowIndex, this.gardenColumnIndex, currentStage, this.currentImageFilePath);
        }

        // Record what happened to the plant
        logger.info("Plant: {} at position ({}, {}) healed by {} points, new health: {}",
                this.botanicalName, this.gardenRowIndex, this.gardenColumnIndex, healAmount, this.vitality);
    }


    public synchronized void setCurrentHealth(int health) {
        int previousStage = getHealthStage();
        int oldHealth = this.vitality;

        int clampedHealth = Math.max(0, Math.min((int) maxHealth, health));
        this.vitality = clampedHealth;

        healthBar.updateHealth(this.vitality);

        if (this.vitality <= 0) {
            EventBus.publish("PlantDeathEvent", this);
            return;
        }

        // additional logic if needed
    }


    /**
     * Changes how the plant looks based on its health.
     * @param stage how healthy the plant is right now.
     */
    private void updatePlantImage(int stage) {
        if (stage >= 0 && stage < this.visualAssetLibrary.size()) {
            this.currentImageFilePath = this.visualAssetLibrary.get(stage);
            EventBus.publish("PlantImageUpdateEvent", new PlantImageUpdateEvent(this));
        }
    }

    /**
     * Figures out if the plant is small, medium, or fully grown.
     * @return a number: 0 for small, 1 for medium, 2 for fully grown.
     */
    private int getHealthStage() {
        if (this.vitality < this.intermediateHealthThreshold) {
            return 0; // Tiny sprout
        } else if (this.vitality < this.peakHealthThreshold) {
            return 1; // Growing nicely
        } else {
            return 2; // Fully grown
        }
    }

    // Easy way to get plant size in words
    public String getGrowthStageDescription() {
        if (this.getCurrentHealth() < this.getHealthMedium()) {
            return "Small";
        } else if (this.getCurrentHealth() < this.getHealthFull()) {
            return "Medium";
        } else {
            return "Full";
        }
    }

    // Simple ways to get and set plant information

    public ArrayList<String> getVulnerableTo() {
        return threatList;
    }

    public String getName() {
        return botanicalName;
    }

    public Boolean getIsWatered() {
        return isFullyHydrated;
    }

    public synchronized void  setIsWatered(Boolean isWatered) {
        this.isFullyHydrated = isWatered;
    }

    public int getCurrentWater() {
        return collectedMoisture;
    }

    public void setCurrentWater(int currentWater) {
        this.collectedMoisture = currentWater;
    }



    public int getWaterRequirement() {
        return hydrationRequirement;
    }


    public String getCurrentImage() {
        return currentImageFilePath;
    }

    public void setCurrentImage(String currentImage) {
        this.currentImageFilePath = currentImage;
    }

    public int getTemperatureRequirement() {
        return climateRequirement;
    }

    public int getRow() {
        return gardenRowIndex;
    }

    public void setRow(int row) {
        this.gardenRowIndex = row;
    }

    public int getCol() {
        return gardenColumnIndex;
    }

    public void setCol(int col) {
        this.gardenColumnIndex = col;
    }

    public int getHealthSmall() {
        return baseHealthThreshold;
    }

    public int getHealthMedium() {
        return intermediateHealthThreshold;
    }

    public int getHealthFull() {
        return peakHealthThreshold;
    }

    public ArrayList<String> getAllImages() {
        return visualAssetLibrary;
    }





    /**
     * Gets how healthy the plant is right now.
     * Safe to use with multiple garden systems running.
     * @return the plant's current health points.
     */
    public synchronized int getCurrentHealth() {
        return vitality;
    }

    // Old code kept just in case we need it later
    // Sets plant health directly - not used right now





}

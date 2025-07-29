package com.example.ooad_project.Parasite.Children;

import com.example.ooad_project.Events.ParasiteDamageEvent;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

// Slow moving slimy creature that often slides past its target
public class Slugs extends Parasite {
    private static final double ATTACK_FAILURE_RATE = 0.25;  // Slides past target 1 out of 4 times
    private Random damageRandomizer = new Random();
    private static final Logger parasiteLogger = LogManager.getLogger("PesticideSystemLogger");

    public Slugs(String name, int damage, String imageName, ArrayList<String> affectedPlants) {
        super(name, damage, imageName, affectedPlants);
    }

    @Override
    public void affectPlant(Plant plant) {
        if (damageRandomizer.nextDouble() >= ATTACK_FAILURE_RATE) {
            // Slug slowly munches on plant leaves
            int previousHealthValue = plant.getCurrentHealth();
            int updatedHealthValue = Math.max(0, plant.getCurrentHealth() - this.getDamage());
            super.publishDamageEvent(new ParasiteDamageEvent(plant.getRow(),plant.getCol(), this.getDamage()));

            plant.setCurrentHealth(updatedHealthValue);
            parasiteLogger.info("Slug has successfully damaged the plant {} at position ({}, {}). Old health: {}. New health: {}",
                    plant.getName(), plant.getRow(), plant.getCol(), previousHealthValue, updatedHealthValue);

        } else {
            // Slug moves too slowly and slides past the plant
            parasiteLogger.info("Slug attempted to damage the plant {} at position ({}, {}) but missed.",
                    plant.getName(), plant.getRow(), plant.getCol());
        }
    }
}
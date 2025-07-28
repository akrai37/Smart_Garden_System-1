package com.example.ooad_project.Parasite.Children;

import com.example.ooad_project.Events.ParasiteDamageEvent;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

// Hungry grasshopper that rarely misses its target
public class Locust extends Parasite {
    private static final double ATTACK_FAILURE_RATE = 0.05;  // Very accurate insect, misses only 1 in 20 times
    private Random damageRandomizer = new Random();
    private static final Logger parasiteLogger = LogManager.getLogger("PesticideSystemLogger");

    public Locust(String name, int damage, String imageName, ArrayList<String> affectedPlants) {
        super(name, damage, imageName, affectedPlants);
    }

    @Override
    public void affectPlant(Plant plant) {
        if (damageRandomizer.nextDouble() >= ATTACK_FAILURE_RATE) {
            // Locust chomps down on the plant leaves
            int previousHealthValue = plant.getCurrentHealth();
            int updatedHealthValue = Math.max(0, plant.getCurrentHealth() - this.getDamage());
            super.publishDamageEvent(new ParasiteDamageEvent(plant.getRow(),plant.getCol(), this.getDamage()));

            plant.setCurrentHealth(updatedHealthValue);
            parasiteLogger.info("Locust has successfully damaged the plant {} at position ({}, {}). Old health: {}. New health: {}",
                    plant.getName(), plant.getRow(), plant.getCol(), previousHealthValue, updatedHealthValue);

        } else {
            // Locust gets distracted and hops away
            parasiteLogger.info("Locust attempted to damage the plant {} at position ({}, {}) but missed.",
                    plant.getName(), plant.getRow(), plant.getCol());
        }
    }
}

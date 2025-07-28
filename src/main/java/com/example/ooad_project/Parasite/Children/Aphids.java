package com.example.ooad_project.Parasite.Children;

import com.example.ooad_project.Events.ParasiteDamageEvent;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;


// Small green bugs that sometimes miss their target
public class Aphids extends Parasite {
    private static final double ATTACK_FAILURE_RATE = 0.10;  // These bugs fail to attack 1 out of 10 times
    private Random damageRandomizer = new Random();
    private static final Logger parasiteLogger = LogManager.getLogger("PesticideSystemLogger");

    public Aphids(String name, int damage, String imageName, ArrayList<String> affectedPlants) {
        super(name, damage, imageName, affectedPlants);
    }

    @Override
    public void affectPlant(Plant plant) {
        if (damageRandomizer.nextDouble() >= ATTACK_FAILURE_RATE) {
            // Successfully hits the plant and causes damage
            int previousHealthValue = plant.getCurrentHealth();
            int updatedHealthValue = Math.max(0, plant.getCurrentHealth() - this.getDamage());
            super.publishDamageEvent(new ParasiteDamageEvent(plant.getRow(),plant.getCol(), this.getDamage()));

            plant.setCurrentHealth(updatedHealthValue);
            parasiteLogger.info("Aphid has successfully damaged the plant {} at position ({}, {}). Old health: {}. New health: {}",
                    plant.getName(), plant.getRow(), plant.getCol(), previousHealthValue, updatedHealthValue);

        } else {
            // Bug misses the target completely
            parasiteLogger.info("Aphid attempted to damage the plant {} at position ({}, {}) but missed.",
                    plant.getName(), plant.getRow(), plant.getCol());
        }
    }
}
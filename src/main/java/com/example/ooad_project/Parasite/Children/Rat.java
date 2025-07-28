package com.example.ooad_project.Parasite.Children;

import com.example.ooad_project.Events.ParasiteDamageEvent;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.Plant.Plant;

import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Sneaky rodent that sometimes gets scared away
public class Rat extends Parasite {

    private Random damageRandomizer = new Random();
    private static final double ATTACK_FAILURE_RATE = 0.15;  // Gets scared and runs away 15% of the time
    private static final Logger parasiteLogger = LogManager.getLogger("PesticideSystemLogger");


    public Rat(String name, int damage , String imageName, ArrayList<String> affectedPlants) {
        super(name, damage, imageName, affectedPlants);
    }


    @Override
    public void affectPlant(Plant plant) {
        if (damageRandomizer.nextDouble() >= ATTACK_FAILURE_RATE) {
            // Rat nibbles on the plant roots and stems
            int previousHealthValue = plant.getCurrentHealth();
            int updatedHealthValue = Math.max(0, plant.getCurrentHealth() - this.getDamage());
            super.publishDamageEvent(new ParasiteDamageEvent(plant.getRow(),plant.getCol(), this.getDamage()));

            plant.setCurrentHealth(updatedHealthValue);
            parasiteLogger.info("Rat has successfully damaged the plant {} at position ({}, {}). Old health: {}. New health: {}",
                    plant.getName(), plant.getRow(), plant.getCol(), previousHealthValue, updatedHealthValue);

        } else {
            // Rat hears a noise and scurries away
            parasiteLogger.info("Rat attempted to damage the plant {} at position ({}, {}) but missed.",
                    plant.getName(), plant.getRow(), plant.getCol());
        }
    }




}

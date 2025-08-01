package com.example.ooad_project.Plant;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {
    private final double maxHealth;
    private double currentHealth;
    private final Label healthLabel;
    private final Rectangle box;

    public HealthBar(double maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        // ✅ Smaller square box
        box = new Rectangle(20, 16);
        box.setFill(Color.LIGHTGREEN);
        box.setStroke(Color.DARKGRAY);
        box.setArcWidth(3);
        box.setArcHeight(3);

        // ✅ Smaller font to fit the new size
        healthLabel = new Label("100%");
        healthLabel.setStyle("-fx-font-size: 6px; -fx-font-weight: bold; -fx-text-fill: black;");

        getChildren().addAll(box, healthLabel);

        // ✅ Prevent auto-resizing by StackPane
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    public void updateHealth(double health) {
        this.currentHealth = Math.max(0, Math.min(maxHealth, health));
        int percentage = (int) Math.round((currentHealth / maxHealth) * 100);
        healthLabel.setText(percentage + "%");

        // ✅ Color based on health thresholds
        if (percentage < 10) {
            box.setFill(Color.RED);
        } else if (percentage < 50) {
            box.setFill(Color.ORANGE);
        } else {
            box.setFill(Color.LIGHTGREEN);
        }
    }
}

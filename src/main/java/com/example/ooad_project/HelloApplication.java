package com.example.ooad_project;

import com.example.ooad_project.API.SmartGardenAPI;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.SubSystems.PesticideSystem;
import com.example.ooad_project.SubSystems.TemperatureSystem;
import com.example.ooad_project.SubSystems.WateringSystem;
import com.example.ooad_project.ThreadUtils.ThreadManager;
import com.example.ooad_project.Parasite.ParasiteManager;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Loading FXML from: " + HelloApplication.class.getResource("/com/example/ooad_project/hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/ooad_project/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        stage.setTitle("DigiGarden - Smart Garden ");
        stage.setScene(scene) ;
        stage.show();
        initializeBackgroundServices();

        // Schedule API rain calls using JavaFX Timeline
        runAPIScheduledTasks();
    }

    private void initializeBackgroundServices() {
        Runnable wateringSystem = new WateringSystem();
        Runnable temperatureSystem = new TemperatureSystem();
        Runnable pesticideSystem = new PesticideSystem();
        DaySystem daySystem = DaySystem.getInstance(); // Initialize DaySystem to start day counter

        ThreadManager.run(wateringSystem);
        ThreadManager.run(temperatureSystem);
        ThreadManager.run(pesticideSystem);

    }

    //temp and rain events are now random
    private void runAPIScheduledTasks() {
        SmartGardenAPI api = new SmartGardenAPI();
        api.initializeGarden();
        Random rand = new Random();

        // Only schedule temperature - rain is handled by WateringSystem
        scheduleRandomTemperature(api, rand);

        // Schedule parasite event every 20 seconds
        ParasiteManager parasiteManager = ParasiteManager.getInstance();
        Timeline parasiteTimeline = new Timeline(new KeyFrame(Duration.seconds(20), ev -> {
            List<Parasite> parasites = parasiteManager.getParasites();
            if (!parasites.isEmpty()) {
                Parasite randomParasite = parasites.get(rand.nextInt(parasites.size()));
                api.parasite(randomParasite.getName());
            }
        }));
        parasiteTimeline.setCycleCount(Timeline.INDEFINITE);
        parasiteTimeline.play();
    }

    private void scheduleRandomTemperature(SmartGardenAPI api, Random rand) {
        int delay = 20 + rand.nextInt(41); // 20–60 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(delay));
        pause.setOnFinished(e -> {
            api.temperature(50 + rand.nextInt(26)); // 50–75 °F
            scheduleRandomTemperature(api, rand); // Reschedule recursively
        });
        pause.play();
    }

    private void runAPIScheduledTasksWithoutJavaFX() {
        SmartGardenAPI api = new SmartGardenAPI();
        api.initializeGarden();
        Random rand = new Random();

        // This is for testing the parasites thread
        ParasiteManager parasiteManager = ParasiteManager.getInstance();

        // Create a scheduled executor service
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

        // Rain is handled by WateringSystem - no need to schedule here

        // Schedule temperature every 40 seconds
        scheduler.scheduleAtFixedRate(() -> {
            int temperature = rand.nextInt(70);
            System.out.println("Changing temperature to: " + temperature);
            api.temperature(temperature);
        }, 0, 40, TimeUnit.SECONDS);

        // Schedule parasite every 10 seconds
        scheduler.scheduleAtFixedRate(() -> {
            List<Parasite> parasites = parasiteManager.getParasites();
            if (!parasites.isEmpty()) {
                Parasite randomParasite = parasites.get(rand.nextInt(parasites.size()));
                System.out.println("Sending parasite: " + randomParasite.getName());
                api.parasite(randomParasite.getName());
            }
        }, 0, 10, TimeUnit.SECONDS);

        // Check garden state every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n------ GARDEN STATE ------");
            api.getState();
            System.out.println("-------------------------\n");
        }, 30, 30, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--no-ui")) {
            // Run without JavaFX UI
            HelloApplication app = new HelloApplication();
            app.initializeBackgroundServices();
            app.runAPIScheduledTasksWithoutJavaFX();

            // Keep the main thread alive
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted: " + e.getMessage());
            }
        } else {
            // Regular JavaFX launch
            launch(args);
        }
    }
}



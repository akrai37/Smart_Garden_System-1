package com.example.ooad_project;

import com.example.ooad_project.API.SmartGardenAPI;
import com.example.ooad_project.Parasite.Parasite;
import com.example.ooad_project.SubSystems.PesticideSystem;
import com.example.ooad_project.SubSystems.TemperatureSystem;
import com.example.ooad_project.SubSystems.WateringSystem;
import com.example.ooad_project.ThreadUtils.ThreadManager;
import com.example.ooad_project.Parasite.ParasiteManager;
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
    public void start(Stage mainApplicationWindow) throws IOException {
        System.out.println("Loading FXML from: " + HelloApplication.class.getResource("/com/example/ooad_project/hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/ooad_project/hello-view.fxml"));
        Scene mainApplicationScene = new Scene(fxmlLoader.load(), 1200, 800);
        mainApplicationScene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        mainApplicationWindow.setTitle("Green Tech - Smart Garden");
        mainApplicationWindow.setScene(mainApplicationScene) ;
        mainApplicationWindow.show();
        initializeBackgroundServices();

        // Start automated environmental simulation tasks for realistic garden behavior
        runAPIScheduledTasks();
    }

    private void initializeBackgroundServices() {
        Runnable waterManagementService = new WateringSystem();
        Runnable temperatureControlService = new TemperatureSystem();
        Runnable pestControlService = new PesticideSystem();
        DaySystem dayTracker = DaySystem.getInstance();

        ThreadManager.run(waterManagementService);
        ThreadManager.run(temperatureControlService);
        ThreadManager.run(pestControlService);

    }


    // Setting up the simulation to mimic real-world garden conditions
    // This method creates realistic environmental changes that a garden might experience
    private void runAPIScheduledTasks() {
        SmartGardenAPI gardenAPI = new SmartGardenAPI();
        gardenAPI.initializeGarden();
        Random weatherRandomizer = new Random();

        // Setting up our virtual pest management system
        ParasiteManager pestManagementSystem = ParasiteManager.getInstance();

        // Simulate natural rainfall patterns - occurs every 60 seconds in our accelerated time
        Timeline weatherSimulation = new Timeline(new KeyFrame(Duration.seconds(60), rainfallEvent -> {
            // Mimicking unpredictable rainfall amounts that gardens experience
            gardenAPI.rain(weatherRandomizer.nextInt(40));
        }));
        weatherSimulation.setCycleCount(Timeline.INDEFINITE);
        weatherSimulation.play();


        // Simulate daily temperature fluctuations that affect plant growth
        Timeline temperatureSimulation = new Timeline(new KeyFrame(Duration.seconds(40), temperatureChangeEvent -> {
            // Creating realistic temperature variations for plant stress testing
            gardenAPI.temperature(weatherRandomizer.nextInt(70));
        }));
        temperatureSimulation.setCycleCount(Timeline.INDEFINITE);
        temperatureSimulation.play();

        // Simulate natural pest occurrences that gardeners face regularly
        Timeline pestOccurrenceSimulation = new Timeline(new KeyFrame(Duration.seconds(10), pestAttackEvent -> {
            List<Parasite> knownGardenPests = pestManagementSystem.getParasites();
            if (!knownGardenPests.isEmpty()) {
                Parasite randomPestThreat = knownGardenPests.get(weatherRandomizer.nextInt(knownGardenPests.size()));
                gardenAPI.parasite(randomPestThreat.getName()); // Introducing a realistic pest challenge
            }
        }));
        pestOccurrenceSimulation.setCycleCount(Timeline.INDEFINITE);
        pestOccurrenceSimulation.play();


    }

    private void runAPIScheduledTasksWithoutJavaFX() {
        SmartGardenAPI simulationAPI = new SmartGardenAPI();
        simulationAPI.initializeGarden();
        Random environmentalRandomizer = new Random();

        // Initializing our comprehensive pest management system for testing
        ParasiteManager pestControlManager = ParasiteManager.getInstance();

        // Creating a multi-threaded task scheduler for realistic garden simulation
        ScheduledExecutorService gardenSimulationScheduler = Executors.newScheduledThreadPool(4);

        // Scheduling realistic rainfall patterns every 60 seconds
        gardenSimulationScheduler.scheduleAtFixedRate(() -> {
            int naturalRainfallAmount = environmentalRandomizer.nextInt(40);
            System.out.println("Nature provides rainfall with amount: " + naturalRainfallAmount);
            simulationAPI.rain(naturalRainfallAmount);
        }, 0, 60, TimeUnit.SECONDS);

        // Scheduling natural temperature fluctuations every 40 seconds
        gardenSimulationScheduler.scheduleAtFixedRate(() -> {
            int dailyTemperatureReading = environmentalRandomizer.nextInt(70);
            System.out.println("Weather system adjusting temperature to: " + dailyTemperatureReading);
            simulationAPI.temperature(dailyTemperatureReading);
        }, 0, 40, TimeUnit.SECONDS);

        // Scheduling realistic pest encounters every 10 seconds
        gardenSimulationScheduler.scheduleAtFixedRate(() -> {
            List<Parasite> environmentalThreats = pestControlManager.getParasites();
            if (!environmentalThreats.isEmpty()) {
                Parasite naturePestChallenge = environmentalThreats.get(environmentalRandomizer.nextInt(environmentalThreats.size()));
                System.out.println("Environmental challenge detected - pest arrival: " + naturePestChallenge.getName());
                simulationAPI.parasite(naturePestChallenge.getName());
            }
        }, 0, 10, TimeUnit.SECONDS);

        // Monitoring garden health status every 30 seconds
        gardenSimulationScheduler.scheduleAtFixedRate(() -> {
            System.out.println("\n------ COMPREHENSIVE GARDEN HEALTH REPORT ------");
            simulationAPI.getState();
            System.out.println("------------------------------------------------\n");
        }, 30, 30, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--no-ui")) {
            // Running the garden simulation in headless mode without visual interface
            HelloApplication smartGardenApplication = new HelloApplication();
            smartGardenApplication.initializeBackgroundServices();
            smartGardenApplication.runAPIScheduledTasksWithoutJavaFX();

            // Keeping the simulation running continuously for comprehensive testing
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException simulationInterruption) {
                System.out.println("Garden simulation interrupted: " + simulationInterruption.getMessage());
            }
        } else {
            // Launching the full interactive garden management interface
            launch(args);
        }
    }
}



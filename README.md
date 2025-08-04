# Smart_Garden_System-1

A Java-based computerized gardening system that simulates plant growth, survival, and health 
under dynamic environmental conditions. Built using Object-Oriented Programming principles, the system 
features modular subsystems for automated watering (including rain simulation), temperature regulation 
and pest detection and treatment.

## Features

- Automated watering system with sprinklers and rain simulation
- Temperature control with cooler behavior during rain
- Parasite management with random pest generation and treatment
- Plant health changes based on water, temperature, and pests
- Simulated day progression every 60 seconds
- Real-time UI built with JavaFX
- Event-driven communication using a custom EventBus system

## Design Patterns Used

- Singleton (e.g., GardenGrid, DaySystem)
- Observer (via EventBus)
- Factory (for dynamic object creation)

## How to Run

### Requirements

- Java 17 or higher
- JavaFX SDK installed
- Maven or IntelliJ IDEA

### Run with UI

Using IntelliJ or any IDE

1. Open the project.
2. Go to `src/main/java/com/example/ooad_project/HelloApplication.java`.
3. Right-click and select **Run 'HelloApplication.main()'**.




package com.example.ooad_project.Plant;

import com.example.ooad_project.Plant.Children.Flower;
import com.example.ooad_project.Plant.Children.Tree;
import com.example.ooad_project.Plant.Children.Vegetable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// Keeps track of all plants by loading data from JSON files
public class PlantManager {
    private static PlantManager managerInstance;
    private List<Flower> bloomingPlants;
    private List<Tree> woodyPlants;
    private List<Vegetable> ediblePlants;

    private PlantManager() {
        bloomingPlants = new ArrayList<>();
        woodyPlants = new ArrayList<>();
        ediblePlants = new ArrayList<>();
        loadPlantsData();
    }

    public static synchronized PlantManager getInstance() {
        if (managerInstance == null) {
            managerInstance = new PlantManager();
        }
        return managerInstance;
    }


    public Plant getPlantByName(String name) {
        // Look through all flowers first
        for (Flower flower : bloomingPlants) {
            if (flower.getName().equals(name)) {
                // Make a fresh copy with the same details
                return new Flower(flower.getName(), flower.getWaterRequirement(), flower.getCurrentImage(),
                        flower.getTemperatureRequirement(), new ArrayList<>(flower.getVulnerableTo()),
                        flower.getHealthSmall(), flower.getHealthMedium(), flower.getHealthFull(), flower.getAllImages());
            }
        }
        // Now check all trees
        for (Tree tree : woodyPlants) {
            if (tree.getName().equals(name)) {
                // Make a fresh copy with the same details
                return new Tree(tree.getName(), tree.getWaterRequirement(), tree.getCurrentImage(),
                        tree.getTemperatureRequirement(), new ArrayList<>(tree.getVulnerableTo()),
                        tree.getHealthSmall(), tree.getHealthMedium(), tree.getHealthFull(), tree.getAllImages());
            }
        }
        // Finally check vegetables
        for (Vegetable vegetable : ediblePlants) {
            if (vegetable.getName().equals(name)) {
                // Make a fresh copy with the same details
                return new Vegetable(vegetable.getName(), vegetable.getWaterRequirement(), vegetable.getCurrentImage(),
                        vegetable.getTemperatureRequirement(), new ArrayList<>(vegetable.getVulnerableTo()),
                        vegetable.getHealthSmall(), vegetable.getHealthMedium(), vegetable.getHealthFull(),vegetable.getAllImages() );
            }
        }
        return null; // Couldn't find that plant anywhere
    }



    private void loadPlantsData() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("plants.json")));
            JSONObject jsonObject = new JSONObject(content);

            loadFlowers(jsonObject.getJSONArray("flowers"));
            loadTrees(jsonObject.getJSONArray("trees"));
            loadVegetables(jsonObject.getJSONArray("vegetables"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFlowers(JSONArray flowerDataArray) {
        for (int i = 0; i < flowerDataArray.length(); i++) {
            JSONObject flowerJsonObject = flowerDataArray.getJSONObject(i);
            ArrayList<String> parasiteVulnerabilities = new ArrayList<>();
            ArrayList<String> resourceImageArray = new ArrayList<>();
            JSONArray vulnerabilityArray = flowerJsonObject.getJSONArray("vulnerableTo");
            JSONArray imageArray = flowerJsonObject.getJSONArray("allImages");

            for (int j = 0; j < vulnerabilityArray.length(); j++) {
                parasiteVulnerabilities.add(vulnerabilityArray.getString(j));
            }
            for (int j = 0; j < imageArray.length(); j++) {
                resourceImageArray.add(imageArray.getString(j));
            }

            bloomingPlants.add(new Flower(
                    flowerJsonObject.getString("name"),
                    flowerJsonObject.getInt("waterRequirement"),
                    flowerJsonObject.getString("currentImage"),
                    flowerJsonObject.getInt("temperatureRequirement"),
                    parasiteVulnerabilities,
                    flowerJsonObject.getInt("healthSmall"),
                    flowerJsonObject.getInt("healthMedium"),
                    flowerJsonObject.getInt("healthFull"),
                    resourceImageArray  // Pass the list of all images
            ));
        }
    }

    // Load tree data from the JSON file
    private void loadTrees(JSONArray treeData) {
        for (int i = 0; i < treeData.length(); i++) {
            JSONObject treeObject = treeData.getJSONObject(i);
            ArrayList<String> threatTypes = new ArrayList<>();
            ArrayList<String> visualResources = new ArrayList<>();
            JSONArray vulnerabilities = treeObject.getJSONArray("vulnerableTo");
            JSONArray images = treeObject.getJSONArray("allImages");

            for (int j = 0; j < vulnerabilities.length(); j++) {
                threatTypes.add(vulnerabilities.getString(j));
            }
            for (int j = 0; j < images.length(); j++) {
                visualResources.add(images.getString(j));
            }

            woodyPlants.add(new Tree(
                    treeObject.getString("name"),
                    treeObject.getInt("waterRequirement"),
                    treeObject.getString("currentImage"),
                    treeObject.getInt("temperatureRequirement"),
                    threatTypes,
                    treeObject.getInt("healthSmall"),
                    treeObject.getInt("healthMedium"),
                    treeObject.getInt("healthFull"),
                    visualResources  // Pass the list of all images
            ));
        }
    }

    // Load vegetable data from the JSON file
    private void loadVegetables(JSONArray vegetableData) {
        for (int i = 0; i < vegetableData.length(); i++) {
            JSONObject cropObject = vegetableData.getJSONObject(i);
            ArrayList<String> vulnerabilityList = new ArrayList<>();
            ArrayList<String> graphicAssets = new ArrayList<>();
            JSONArray vulnerabilities = cropObject.getJSONArray("vulnerableTo");
            JSONArray images = cropObject.getJSONArray("allImages");

            for (int j = 0; j < vulnerabilities.length(); j++) {
                vulnerabilityList.add(vulnerabilities.getString(j));
            }
            for (int j = 0; j < images.length(); j++) {
                graphicAssets.add(images.getString(j));
            }

            ediblePlants.add(new Vegetable(
                    cropObject.getString("name"),
                    cropObject.getInt("waterRequirement"),
                    cropObject.getString("currentImage"),
                    cropObject.getInt("temperatureRequirement"),
                    vulnerabilityList,
                    cropObject.getInt("healthSmall"),
                    cropObject.getInt("healthMedium"),
                    cropObject.getInt("healthFull"),
                    graphicAssets  // Pass the list of all images
            ));
        }
    }

    // Get all flowers we know about
    public List<Flower> getFlowers() {
        return bloomingPlants;
    }

    // Get all trees we know about
    public List<Tree> getTrees() {
        return woodyPlants;
    }

    // Get all vegetables we know about
    public List<Vegetable> getVegetables() {
        return ediblePlants;
    }
}

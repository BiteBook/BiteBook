package com.codeup.bitebook.models;

import com.codeup.bitebook.models.NutritionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EdamamService {
    @Value("${edamam.appId}")
    private String edamamAppId;

    @Value("${edamam.key}")
    private String edamamKey;
    private RestTemplate restTemplate;

    public EdamamService() {
        this.restTemplate = new RestTemplate();
    }

    public NutritionInfo getNutritionInfo(String ingredients) {
        String url = "https://api.edamam.com/api/nutrition-data?app_id=" + edamamAppId + "&app_key=" + edamamKey + "&ingr=" + ingredients;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        NutritionInfo nutritionInfo = new NutritionInfo();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            nutritionInfo.setCalories(root.path("calories").asDouble());
            System.out.println("Calories: " + nutritionInfo.getCalories());

            nutritionInfo.setProtein(root.path("totalNutrients").path("PROCNT").path("quantity").asDouble());
            System.out.println("Protein: " + nutritionInfo.getProtein());

            nutritionInfo.setCarbohydrates(root.path("totalNutrients").path("CHOCDF").path("quantity").asDouble());
            System.out.println("Carbohydrates: " + nutritionInfo.getCarbohydrates());

            nutritionInfo.setFats(root.path("totalNutrients").path("FAT").path("quantity").asDouble());
            System.out.println("Fats: " + nutritionInfo.getFats());

            nutritionInfo.setFibre(root.path("totalNutrients").path("FIBTG").path("quantity").asDouble());
            System.out.println("Fibre: " + nutritionInfo.getFibre());

            nutritionInfo.setSugar(root.path("totalNutrients").path("SUGAR").path("quantity").asDouble());
            System.out.println("Sugar: " + nutritionInfo.getSugar());

            nutritionInfo.setSodium(root.path("totalNutrients").path("NA").path("quantity").asDouble());
            System.out.println("Sodium: " + nutritionInfo.getSodium());


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Response body: " + response.getBody());
        }

        return nutritionInfo;
    }
}
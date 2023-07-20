package com.codeup.bitebook.models;

import com.codeup.bitebook.models.NutritionInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EdamamService {
    private RestTemplate restTemplate;

    public EdamamService() {
        this.restTemplate = new RestTemplate();
    }

    public NutritionInfo getNutritionInfo(String ingredients) {
        String url = "https://api.edamam.com/api/nutrition-data?app_id=" + "46040ebe" + "&app_key=" + "d145f8770c0e24f240003773c6b6ea40" + "&ingr=" + ingredients;

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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Response body: " + response.getBody());
        }

        return nutritionInfo;
    }
}

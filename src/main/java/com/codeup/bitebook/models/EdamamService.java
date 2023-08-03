package com.codeup.bitebook.models;

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

    private final RestTemplate restTemplate;
    public double totalCalories = 0;
    public double totalProtein = 0;
    public double totalFibre = 0;
    public double totalFat = 0;
    public double totalSugar = 0;
    public double totalCarbs = 0;
    public double totalSodium = 0;

    public EdamamService() {
        restTemplate = new RestTemplate();
    }

    public void getNutritionInfo(String ingredients) {
        String url = "https://api.edamam.com/api/nutrition-data?app_id=" + edamamAppId + "&app_key=" + edamamKey + "&ingr=" + ingredients;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            System.out.println(root);
            totalCalories += (root.path("calories").asDouble());
            totalProtein += (root.path("totalNutrients").path("PROCNT").path("quantity").asDouble());
            totalCarbs += (root.path("totalNutrients").path("CHOCDF").path("quantity").asDouble());
            totalFat += (root.path("totalNutrients").path("FAT").path("quantity").asDouble());
            totalFibre += (root.path("totalNutrients").path("FIBTG").path("quantity").asDouble());
            totalSugar += (root.path("totalNutrients").path("SUGAR").path("quantity").asDouble());
            totalSodium += (root.path("totalNutrients").path("NA").path("quantity").asDouble());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Response body: " + response.getBody());
        }


    }
    public NutritionInfo allNutrition(String ingredientList){
        totalCalories = 0;
        totalProtein = 0;
        totalFibre = 0;
        totalFat = 0;
        totalSugar = 0;
        totalCarbs = 0;
        totalSodium = 0;
        NutritionInfo nutritionInfo = new NutritionInfo();
        String[] ingredientArr = ingredientList.split(",");
        for (String s : ingredientArr) {
            getNutritionInfo(s);
        }

        nutritionInfo.setCalories(Math.round(totalCalories));
        nutritionInfo.setProtein(Math.round(totalProtein));
        nutritionInfo.setCarbohydrates(Math.round(totalCarbs));
        nutritionInfo.setFats(Math.round(totalFat));
        nutritionInfo.setFibre(Math.round(totalFibre));
        nutritionInfo.setSugar(Math.round(totalSugar));
        nutritionInfo.setSodium(Math.round(totalSodium));
        return nutritionInfo;
    }


}
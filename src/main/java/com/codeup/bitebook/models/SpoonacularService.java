package com.codeup.bitebook.models;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class SpoonacularService {

    @Autowired
    private RestTemplate restTemplate;

    public SpoonacularService() {
        this.restTemplate = new RestTemplate();
    }

    @Value("4d1bf95e5a584ed1805759872ebe2313")
    private String spoonKey;

    public ResponseEntity<String> searchRecipes(String query) {
        String url = "https://api.spoonacular.com/recipes/complexSearch?apiKey=" + spoonKey + "&query=" + query;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    public ResponseEntity<String> getRecipeDetails(String recipeId) {
        String url = "https://api.spoonacular.com/recipes/" + recipeId + "/information?apiKey=" + spoonKey;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }

    // Add more methods

}

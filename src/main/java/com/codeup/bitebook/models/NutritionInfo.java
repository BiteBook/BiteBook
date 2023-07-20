package com.codeup.bitebook.models;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter

public class NutritionInfo {


    private double calories;
    private double protein;
    private double carbohydrates;
}

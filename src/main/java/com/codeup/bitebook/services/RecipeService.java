package com.codeup.bitebook.services;

import java.util.List;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository repo;

    public List<Recipe> listAll(String keyword){
        if (keyword != null){
            return repo.findAll(keyword);
        }
        return repo.findAll();
    }
//    public void save(Recipe recipe){
//        repo.save(recipe);
//    }
}

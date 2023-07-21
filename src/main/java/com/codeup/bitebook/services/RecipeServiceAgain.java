package com.codeup.bitebook.services;

import com.codeup.bitebook.models.Recipe;
import com.codeup.bitebook.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceAgain {
    @Autowired
    private RecipeRepository repo;

    public List<Recipe> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }
}

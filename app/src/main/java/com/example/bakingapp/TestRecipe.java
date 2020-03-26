package com.example.bakingapp;

import java.util.ArrayList;

public class TestRecipe {

    String id;
    String name;
    ArrayList<TestIngredient> ingredients;
    ArrayList<TestStep> steps;
    String servings;
    String image;

    public TestRecipe(
            String id, String name, ArrayList<TestIngredient> ingredients, ArrayList<TestStep> step, String servings, String image
    ) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = step;
        this.servings = servings;
        this.image = image;
    }

    public String getId() {
        return id;
    }
}
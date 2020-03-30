package com.example.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Feed {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("servings")
    @Expose
    private String servings;

    @SerializedName("image")
    @Expose
    private String image;

    private ArrayList<Ingredients> ingredients;

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Steps> steps) {
        this.steps = steps;
    }

    private ArrayList<Steps> steps;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id + '\'' + ", name=" + name + "\''" + "ingredients=" + ingredients + "steps=" + steps + "}";
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<Steps> getSteps() {
        return steps;
    }

    public Feed loadRecipeData() {
        Feed f = new Feed();
        f.id = id;
        f.servings = servings;
        f.name = name;
        f.image = image;
        f.steps = getSteps();
        f.ingredients = getIngredients();
        return f;
    }
}

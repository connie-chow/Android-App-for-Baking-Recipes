package com.example.bakingapp;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipes {

    int getId;

    @PrimaryKey
    @NonNull
    private String r_id;

    private String id;
    private String name;
    private String ingredients;
    private String steps;
    private String servings;
    private String image;

    // Constructor for creating movie entry object to insert into database
    public Recipes (
            String r_id,
            String name,
            String ingredients,
            String steps,
            String servings,
            String image) {

        this.r_id = r_id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
    }


    // Write/Read these private values
    public String getId() { return r_id; }
    public String getName() { return name; }
    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getServings() { return servings; }

    public void setId(String s) { this.r_id = s; }
    public void setName(String s) { this.name = s; }
    public void setIngredients(String s) { this.ingredients = s; }
    public void setSteps(String s) { this.steps = s; }
    public void setServings(String s) { this.servings = s; }

}

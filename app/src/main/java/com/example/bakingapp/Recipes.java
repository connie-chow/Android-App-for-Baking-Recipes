package com.example.bakingapp;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "recipes")
public class Recipes {

    /** The name of the Cheese table. */
    public static final String TABLE_NAME = "recipes";

    int getId;

    @PrimaryKey
    @NonNull
    private String r_id;

    private String id;
    private String name;
    //private Ingredients ingredients;
    //private Steps steps;
    private String servings;
    private String image;

    // Constructor for creating movie entry object to insert into database
    public Recipes (
            String r_id,
            String name,
            //ArrayList<Ingredients> ingredients,
            //ArrayList<Steps> steps,
            String servings,
            String image) {

        this.r_id = r_id;
        this.name = name;
        //this.ingredients.setList(ingredients);
        //this.steps = steps;
        this.servings = servings;
        this.image = image;
    }


    // Write/Read these private values
    public String getR_id() { return r_id; }
    public String getId() { return r_id; }
    public String getName() { return name; }
    //public Ingredients getIngredients() { return ingredients; }
    //public Steps getSteps() { return steps; }
    public String getServings() { return servings; }
    public String getImage() { return image; }

    public void setR_id(String r) { this.r_id = r; }
    public void setId(String s) { this.r_id = s; }
    public void setName(String s) { this.name = s; }
    //public void setIngredients(Ingredients s) { this.ingredients = s; }
    //public void setSteps(Steps s) { this.steps = s; }
    public void setServings(String s) { this.servings = s; }
    public void setImage(String m) { this.image = m; }
}

package com.example.bakingapp;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredients")
public class Ingredients {

    int getId;

    @PrimaryKey
    @NonNull
    private String i_id;

    private String quantity;
    private String measure;
    private String ingredient;


    // Constructor for creating movie entry object to insert into database
    public Ingredients(
            String i_id,
            String quantity,
            String measure,
            String ingredient) {

        this.i_id = i_id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }


    // Write/Read these private values
    public String getId() { return i_id; }
    public String getQuantity() { return quantity; }
    public String getMeasure() { return measure; }
    public String getIngredient() { return ingredient; }

    public void setId(String s) { this.i_id = s; }
    public void setQuantity(String s) { this.quantity = s; }
    public void setMeasure(String s) { this.measure = s; }
    public void setIngredient(String s) { this.ingredient = s; }

}

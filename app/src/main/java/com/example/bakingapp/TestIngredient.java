package com.example.bakingapp;

import java.util.ArrayList;

// one ingredient in a recipe
public class TestIngredient {

    String i_id;
    String quantity;
    String measure;
    String ingredient;

    public TestIngredient ( String id, String quantity, String measure, String ingredient ) {
        this.i_id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

}

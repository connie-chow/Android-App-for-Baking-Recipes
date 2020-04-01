package com.example.bakingapp;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//https://stackoverflow.com/questions/47130307/how-to-make-composite-key-in-room-while-using-mvvm-in-android
@Entity(tableName = "ingredients", primaryKeys = {"r_id","i_id"})
public class Ingredients {

    int getId;

    @ColumnInfo
    @NonNull
    private String i_id;

    @ColumnInfo
    @NonNull
    private String r_id; // recipe id this ingredient belongs to


    private String quantity;
    private String measure;
    private String ingredient;


    // Constructor for creating movie entry object to insert into database
    public Ingredients(
            String r_id,
            String i_id,
            String quantity,
            String measure,
            String ingredient) {

        this.r_id = r_id;
        this.i_id = i_id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }


    // Write/Read these private values
    public String getI_id() { return i_id; }
    public String getId() { return i_id; }
    public String getQuantity() { return quantity; }
    public String getMeasure() { return measure; }
    public String getIngredient() { return ingredient; }
    public String getR_id() { return r_id; }

    public int getGetId() {
        return getId;
    }

    public void setGetId(int getId) {
        this.getId = getId;
    }

    public void setI_id(String i) { this.i_id = i; }
    public void setId(String s) { this.i_id = s; }
    public void setQuantity(String s) { this.quantity = s; }
    public void setMeasure(String s) { this.measure = s; }
    public void setIngredient(String s) { this.ingredient = s; }
    public void setR_id(String r) { this.r_id = r; }
}

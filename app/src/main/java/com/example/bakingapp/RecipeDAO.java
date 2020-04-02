package com.example.bakingapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.MutableLiveData;
import androidx.room.*;
import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;


/*
DAO = Data Access Object
Provides simple interface to the SQLite database underneath
 */
@Dao
public interface RecipeDAO {


    @Query("SELECT * FROM recipes ORDER BY id ASC")
    LiveData<List<Recipes>> getAllRecipes();

    @Query("SELECT * FROM ingredients ORDER BY r_id ASC")
    List<Ingredients> getAllIngredients();

    @Query("SELECT * FROM steps ORDER BY r_id ASC")
    List<Steps> getAllSteps();

    @Delete
    void deleteRecipes(Recipes r);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertRecipe(Recipes r);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertIngredients(Ingredients i);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSteps(Steps s);

    @Query("SELECT * from recipes WHERE id = :r_id")
    Recipes loadRecipeByMId(String r_id);

    @Query("UPDATE recipes SET name = :name WHERE r_id = :r_id")
    int updateRecipe(String r_id, String name);

    @Query("SELECT * FROM steps WHERE r_id = :r_id")
    LiveData<List<Steps>> loadStepsByRecipeId(String r_id);

    @Query("SELECT * FROM ingredients WHERE r_id =:r_id")
    LiveData<List<Ingredients>> loadIngredientsByRecipeId(String r_id);

    @Update(onConflict = REPLACE)
    void updateRecipe(Recipes r);

    @Query("DELETE FROM recipes")
    void deleteAll();

    @Query("DELETE FROM steps WHERE r_id = :r_id")
    void deleteAllStepsByRId(String r_id);

    @Query("DELETE FROM ingredients WHERE r_id =:r_id")
    void deleteAllIngredientsByRId(String r_id);

    @Query("DELETE FROM steps")
    void deleteALLSteps();

    @Query("DELETE FROM ingredients")
    void deleteAllIngredients();


}


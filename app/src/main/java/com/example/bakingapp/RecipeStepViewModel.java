package com.example.bakingapp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;


/*
  ViewModel acts as intermediary between UI and the database
  Used for accessing data across Android Lifecycle
  So even if user rotates screen or pauses, we will still be able to access data from the View Model
 */
public class RecipeStepViewModel extends AndroidViewModel {

    //private static long String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Steps>> mRecipeSteps;
    private LiveData<List<Ingredients>> mRecipeIngredients;
    private LiveData<Steps> mRecipeStepDetails;
    private AppDatabase database;
    private RecipeRepository mRepo;
    private Context mContext = getApplication().getApplicationContext();


    /*
        Use constructor to initialize all data that UI needs to populate
     */
    public RecipeStepViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        mRepo = new RecipeRepository(application);
        //mRecipeSteps = mRepo.getRecipeSteps(recipeId);
    }

    LiveData<List<Steps>> getRecipeSteps(String recipeId) {
        mRecipeSteps = mRepo.getRecipeSteps(recipeId);
        return mRecipeSteps;
    }


    LiveData<List<Ingredients>> getRecipeIngredients(String recipeId) {
        mRecipeIngredients = mRepo.getRecipeIngredients(recipeId);
        return mRecipeIngredients;
    }


    LiveData<Steps> getRecipeStepDetails(String recipeId, String stepId) {
        mRecipeStepDetails = mRepo.getRecipeStepDetails(recipeId, stepId);
        return mRecipeStepDetails;
    }



}


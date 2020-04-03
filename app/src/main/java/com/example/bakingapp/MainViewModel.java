package com.example.bakingapp;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;


/*
  ViewModel acts as intermediary between UI and the database
  Used for accessing data across Android Lifecycle
  So even if user rotates screen or pauses, we will still be able to access data from the View Model
 */
public class MainViewModel extends AndroidViewModel {

    //private static long String TAG = MainViewModel.class.getSimpleName();
    private MutableLiveData<List<Recipes>> mRecipes;
    private LiveData<List<Recipes>> mAllRecipes;
    private AppDatabase database;
    private RecipeRepository mRepo;
    private Context mContext = getApplication().getApplicationContext();



    /*
        Use constructor to initialize all data that UI needs to populate
     */
    public MainViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        mRepo = new RecipeRepository(application);
        mAllRecipes =mRepo.getAllRecipes();
//        recipes = database.recipeDAO().getAllRecipes();
    }


    LiveData<List<Recipes>> getAllRecipes() { return mAllRecipes; }


    public void init() {
        if(mRecipes != null) {
            return;
        }
        //mRepo = RecipeRepository.getInstance();
        //mRecipes = mRepo.getRecipes(mContext);
        //mRecipes = database.recipeDAO().getAllRecipes3();
    }


    // LiveData can only be observed
    public MutableLiveData<List<Recipes>> getRecipes() {
        return mRecipes;
    }


}


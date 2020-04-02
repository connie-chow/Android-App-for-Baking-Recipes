package com.example.bakingapp;

import android.app.Application;
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
    public LiveData<List<Recipes>> recipes;
    private AppDatabase database;
    public MutableLiveData<List<Recipes>> mutableRecipes = new MutableLiveData<List<Recipes>>();
    public MutableLiveData<List<Ingredients>> mutableIngredients = new MutableLiveData<List<Ingredients>>();
    public MutableLiveData<List<Steps>> mutableSteps = new MutableLiveData<List<Steps>>();


    /*
        Use constructor to initialize all data that UI needs to populate
     */
    public MainViewModel(@NonNull Application application) {
        super(application);

        // how do I know this was initialized correctly?
        database = AppDatabase.getInstance(application);
        //this.getApplication()
        //database = AppDatabase.getInstance(application);
        //movies is null after calling ViewModelProvider constructor

        // what are the values in movies?
        // _movieDao = null, mDatabase = null
        //movies = database.movieDao().getAllMovies();
        //mutableRecipes.setValue((List<Recipes>)database.recipeDAO().getAllRecipes()); //.getValue()
        //recipes = mutableRecipes;

        recipes = database.recipeDAO().getAllRecipes();

    }


    // Loads most popular movies
    public void loadData() {
        //MainViewModel.FetchMovieTask movies = new MainViewModel.FetchMovieTask();
        //movies.execute("popular");
        // Assign to movies
        // should call repository to load the async task and put into Room DB
    }

    public LiveData<List<Recipes>> getAllRecipes() {
        //mutableRecipes.setValue((List<Recipes>) database.recipeDAO().getAllRecipes());  //getValue());
        //return mutableRecipes;
        recipes = (LiveData<List<Recipes>>) database.recipeDAO().getAllRecipes();
        return recipes;
    }

    public LiveData<List<Recipes>> getRecipes() {
        return recipes;
    }

}


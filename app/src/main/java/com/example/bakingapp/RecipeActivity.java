package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
        //extends FragmentActivity  {

        //implements RecipeFragment.ButtonClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent i = getIntent();
        String m_id = i.getExtras().getString("id");


        // Instantiate Fragment Manager in charge of fragments associated with this Activity
        // What is the concept behind a transaction and committing to it?
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeFragment recipeFragment = new RecipeFragment();
        fragmentTransaction.add(R.id.recipe_container, recipeFragment).commit();

        // https://stackoverflow.com/questions/14880746/difference-between-sw600dp-and-w600dp
        // https://developer.android.com/guide/practices/screens_support.html
        // https://stackoverflow.com/questions/16706076/different-resolution-support-android
        // https://developer.android.com/training/multiscreen/screensizes


        // If tablet, set the recipe details on the right pane
        if (findViewById(R.id.recipe_details_container) != null) {
            Bundle arguments = new Bundle();
            //arguments.putInt(recipeDetailsFragment.ARG_ITEM_POS, 0);
            //RecipesDetailFragment fragment = new RecipesDetailFragment();
            //fragment.setArguments(arguments);
            //getSupportFragmentManager().beginTransaction().add(R.id.fragment_recipe_details, fragment).commit();
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
            fragmentManager.beginTransaction().add(R.id.recipe_details_container, recipeDetailsFragment).commit();
        }
    }


    // to be implemented
    public void onButtonClick(String arg1, int arg2) {
        // implement code for callback method
    }
}

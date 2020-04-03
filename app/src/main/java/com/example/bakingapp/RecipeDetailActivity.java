package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Get incoming intent with bundle data, get Step ID to retrieve details
        Intent i = getIntent();
        String b = i.getExtras().getString("step_id");
        String c = i.getExtras().getString("recipe_id");

        // Pretend to get step details and step media
        // Pass it to the fragment...
        //if (savedInstanceState == null) {

// instantiate MovieDetailFragment and then take the Bundle data and set to the fragment
            //getSupportFragmentManager().beginTransaction().add(R.id.frag_recipe_details, fragment).commit();


        // mobile
        if (findViewById(R.id.recipe_details_container) != null) {
            RecipeDetailsFragment recipeFragment = new RecipeDetailsFragment();
            Bundle arguments = new Bundle();
            arguments.putString("step_id", b);
            arguments.putString("recipe_id", c);
            recipeFragment.setArguments(arguments);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.recipe_details_container, recipeFragment).commit();
        }



        // Instantiate Fragment Manager in charge of fragments associated with this Activity
        // What is the concept behind a transaction and committing to it?


        // https://stackoverflow.com/questions/14880746/difference-between-sw600dp-and-w600dp
        // https://developer.android.com/guide/practices/screens_support.html
        // https://stackoverflow.com/questions/16706076/different-resolution-support-android
        // https://developer.android.com/training/multiscreen/screensizes


        //https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android

    }

}

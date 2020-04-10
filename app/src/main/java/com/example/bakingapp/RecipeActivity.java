package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

        Bundle data = new Bundle();
        data.putString("recipe_id", m_id);


        // Instantiate Fragment Manager in charge of fragments associated with this Activity
        // What is the concept behind a transaction and committing to it?
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(data);
        fragmentTransaction.add(R.id.recipe_container, recipeFragment).commit();

        // https://stackoverflow.com/questions/14880746/difference-between-sw600dp-and-w600dp
        // https://developer.android.com/guide/practices/screens_support.html
        // https://stackoverflow.com/questions/16706076/different-resolution-support-android
        // https://developer.android.com/training/multiscreen/screensizes


        // If tablet, set the recipe details on the right pane
        // Tablet layout xml will contain recipe_details_container
        if (findViewById(R.id.recipe_details_container) != null) {
            // default to first recipe step in this fragment for loading
            Bundle arguments = new Bundle();
            arguments.putString("step_id", "0");
            arguments.putString("recipe_id", "1");
            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
            recipeDetailsFragment.setArguments(arguments);
            fragmentManager.beginTransaction().add(R.id.recipe_details_container, recipeDetailsFragment).commit();
        }
    }


    // switchContent
    // param: Fragment fragment
    /*
    public void switchContent(int id, String recipe_id, String step_id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RecipeDetailsFragment recipeDetailsFragment =  new RecipeDetailsFragment();
        Bundle b = new Bundle();
        b.putString("recipe_id", recipe_id);
        b.putString("step_id", step_id);
        recipeDetailsFragment.setArguments(b);
        ft.replace(id, recipeDetailsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
*/

    // to be implemented
    public void onButtonClick(String arg1, int arg2) {
        // implement code for callback method
    }
}

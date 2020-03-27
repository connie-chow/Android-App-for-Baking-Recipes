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
/*
        // Room Database access
        mDb = AppDatabase.getInstance(getApplicationContext());


        // tell layout manager we want it to layout contents of recycler view as Linear Layout
        RecyclerView trailersView = (RecyclerView) findViewById(R.id.rv_trailers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailersView.setLayoutManager(layoutManager);
        mMovieTrailersAdapter = new TrailerAdapter(this, new ArrayList<MovieRoom>());
        trailersView.setAdapter(mMovieTrailersAdapter);


        RecyclerView reviewsView = (RecyclerView) findViewById(R.id.rv_reviews);
        LinearLayoutManager layoutManagerReviews = new LinearLayoutManager(this);
        reviewsView.setLayoutManager(layoutManagerReviews);
        mMovieReviewsAdapter = new ReviewsAdapter(this, new ArrayList<Pair<String, String>>());
        reviewsView.setAdapter(mMovieReviewsAdapter);


        // Get Movie ID from Intent
        Intent i = getIntent();
        final String m_id = i.getExtras().getString("id");



        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                final MovieRoom m = mDb.movieDao().loadMovieByMId(m_id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // to do
                        populateUI(m);
                    }

                    // New data is back from the server. Hooray!
                    //mMovieAdapter.notifyDataSetChanged();
                });
            }
        });
   */



        Intent i = getIntent();
        String s_id = i.getExtras().getString("id");


        // Instantiate Fragment Manager in charge of fragments associated with this Activity
        // What is the concept behind a transaction and committing to it?
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecipeDetailsFragment recipeFragment = new RecipeDetailsFragment();
        fragmentTransaction.add(R.id.recipe_details_container, recipeFragment).commit();

        // https://stackoverflow.com/questions/14880746/difference-between-sw600dp-and-w600dp
        // https://developer.android.com/guide/practices/screens_support.html
        // https://stackoverflow.com/questions/16706076/different-resolution-support-android
        // https://developer.android.com/training/multiscreen/screensizes

    }

}

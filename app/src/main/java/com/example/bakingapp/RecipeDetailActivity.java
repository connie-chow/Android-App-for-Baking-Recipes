package com.example.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
/*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

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
    }

}

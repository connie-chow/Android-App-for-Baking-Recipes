package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

/*
    RecyclerView must have an adapter defined and based on RecyclerView.Adapter class
    Adapter manages ViewHolder objects by creating ViewHolder objects as needed and binds
    ViewHolders to their data
    Assigns ViewHolder to position in list and then calls onBindViewHolder()
    As user scrolls, the RecyclerView creates new ViewHolders as necessary
    https://developer.android.com/guide/topics/ui/layout/recyclerview
 */

    private Context mContext;
    private LayoutInflater inflater;
    //private ArrayList<TestStep> mSteps;
    private ArrayList<Steps> mRecipeSteps;


    //public RecipeStepAdapter(Context context, ArrayList<TestStep> recipes) {
    public RecipeStepAdapter(Context context, ArrayList<Steps> recipes) {
        super();
        this.mContext = context;
        //this.mSteps = new ArrayList<TestStep>(recipes);
        this.mRecipeSteps = new ArrayList<Steps>(recipes);
        inflater = LayoutInflater.from(context);
    }


    // getCount() very important, it is called and size of ImageAdapter ArrayList checked, if 0
    // the RecyclerView will not create any ViewHolders
    public int getCount() {
        return mRecipeSteps.size();
    }

    public void clear() { mRecipeSteps.clear(); }
    public Steps getItem(int position) {
        return mRecipeSteps.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }

    // Sets list of movies to the adapter
    /*
    public void setList(List<TestStep> movies)
    {
        if(movies != null){
            for(TestStep movieObj : movies) {
                mSteps.add(movieObj);
            }
            this.notifyDataSetChanged();
        }
    }

     */


    public void setList(List<Steps> steps) {
        if(steps != null) {
            for(Steps s : steps ) {
                mRecipeSteps.add(s);
            }
            this.notifyDataSetChanged();
        }
    }

    // Add MovieRoom entity to adapter
    public void add(TestStep m) {
/*
        String poster_path = m.getPoster_path();
        String adult = m.getAdult();
        String overview = m.getOverview();
        String release_date = m.getRelease_date();
        String genre_ids = m.getGenre_ids();
        String m_id = m.getM_id();
        String original_title = m.getOriginal_title();
        String original_language = m.getOriginal_language();
        String title = m.getTitle();
        String backdrop_path = m.getBackdrop_path();
        String popularity = m.getPopularity();
        String vote_count = m.getVote_count();
        String vote_average = m.getVote_average();
        String video = m.getVideo();
        boolean favorite = m.getFavorite();

        mSteps.add(m);
        notifyDataSetChanged();

 */
        //mSteps.add(m);
        notifyDataSetChanged();
    }


    // Constructor
    public RecipeStepAdapter(Context c) {
        mContext = c;
    }

    // switchContent
    // param: Fragment fragment
    /*
    public void switchContent(int id, String recipe_id, String step_id) {
        if (mContext == null)
            return;
        if (mContext instanceof RecipeActivity) {
            RecipeActivity mainActivity = (RecipeActivity) mContext;
            //Fragment frag = fragment;
            RecipeActivity.switchContent(id, recipe_id, step_id);
        }

    }*/


    //////////////////////////////////////////////////////////////////////////
    // MovieViewHolder class
    // ViewHolder is an item view which is an item in the RecyclerView
    // findViewById() calls done here and one time instead of many calls
    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Initialize the specific view for this MovieViewHolder
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_step;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeStepViewHolder viewHolder = new RecipeStepViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        //Bind each item to the correspond views
        holder.bind(mRecipeSteps.get(position));
        //TextView recipeStepText = findViewById(R.id.recipe_step);
        /*
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentJump(item);


            }
        });
         */
    }


    // this gets called right after MainActivity onCreate() and GridLayout Manager is created
    // RecyclerView instantiated and triggered this to get size
    @Override
    public int getItemCount()
    {
        return mRecipeSteps != null ? mRecipeSteps.size(): 0;
    }



    /*
        Views/items in the list are represented by ViewHolder objects which extend from RecyclerView.ViewHolder
     */
    public class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView mRecipeStep;


        // Constructor: Assign reference to grid element and set a click listener to it
        RecipeStepViewHolder(View itemView) {
            super(itemView);
            mRecipeStep = itemView.findViewById(R.id.recipe_step);
            mRecipeStep.setOnClickListener(this);

        }


        // Bind the ViewHolder to Adapter content by assigning adapter movie data to the movie
        // thumbnail UI element reference
        void bind(Steps recipe) {
            //Use Picasso to load each image
            // load from adapter variable the particular movie
            //Picasso.get().setLoggingEnabled(true);
            // when i changed http to https, then the image loaded
            //Picasso.get().load("https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg").into(mRecipeThumbnail);
            //String movie_url = "https://image.tmdb.org/t/p/w185/" + movie.getPoster_path();
            //Picasso.get().load(movie_url).into(mRecipeThumbnail);
            //https://github.com/square/picasso/issues/427
            /*
            String url = "https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg";
             */
            /*
            Glide.with(itemView)  //2
                    .load(url) //3
                    .centerCrop() //
                    .into(mRecipeThumbnail); //8

             */

            /*
            .placeholder(R.drawable.ic_image_place_holder) //5
                    .error(R.drawable.ic_broken_image) //6
                    .fallback(R.drawable.ic_no_image) //7
             */

            mRecipeStep.setText(recipe.getDescription());
        }


        @Override
        public void onClick(View v) {

            //https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
            //We need to send the proper url for the image so that when the DetailActivity opens it has the proper image url

            Intent i = new Intent(mContext, RecipeDetailActivity.class);
            Steps m = mRecipeSteps.get(getAdapterPosition());
            Bundle arguments = new Bundle();

            // unable to see parent RecipeActivity's xml layout...so will always be null
            //if (v.findViewById(R.id.recipe_details_container) == null) {
            LinearLayout parentActivity = (LinearLayout) v.getParent();
            //FragmentManager fragMgr = parentActivity.getSupportFragmentManager();
            if(parentActivity.findViewById(R.id.recipe_details_container) == null) {

                // Best practice design is to pass the id of the object we are navigating to instead
                // of the whole object which is expensive
                i.putExtra("step_id", m.getS_id());
                i.putExtra("recipe_id", m.getR_id());

                arguments.putString("step_id", m.getS_id());
                arguments.putString("recipe_id", m.getR_id());

                //mContext.startActivity(i);
            } else {
                // if two pane layout
                // get view by id for recipe details and set the step id by bundle

                // replace detail fragment
                // if fragment exists, use Bundle to pass data
                // https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
                // https://stackoverflow.com/questions/50378974/cannot-resolve-method-getsupportfragmentmanager
                // Cannot invoke Fragment Manager in adapter...has to be done in am Activity extends FragmentActivity
                // or AppCompatActivity

                arguments.putString("step_id", m.getS_id());
                arguments.putString("recipe_id", m.getR_id());
                i.putExtras(arguments);
                //https://stackoverflow.com/questions/768969/passing-a-bundle-on-startactivity
                //https://stackoverflow.com/questions/35007764/pass-bundle-from-recycleview-adapter-to-activity

            }


            //https://stackoverflow.com/questions/28984879/how-to-open-a-different-fragment-on-recyclerview-onclick
            if (v.findViewById(R.id.recipe_details_container) == null) {
               // mContext.startActivity(i);
                // already set the Bundle with recipe_id and step_id and sent to RecipeDetailActivity...
                //switchContent(R.id.recipe_details_container, m.getR_id(), m.getS_id()); //, mFragment);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                RecipeDetailsFragment myFragment = new RecipeDetailsFragment();
                myFragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.recipe_container, myFragment).addToBackStack(null).commit();
// chnaged from recipe_details_container, you use the current container to contain the new fragment
            }
        }
    }
}

/*
If you are interested there is also anther library called Glide which can compete with Picasso in certain aspects:

Glide ➡️ https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
Picasso vs Glide ➡️ https://yourwebsitefirst.com/picasso-vs-glide-advantage-disadvantage/

 */


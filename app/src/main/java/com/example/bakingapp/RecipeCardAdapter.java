package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {

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
    private ArrayList<Recipes> mRecipes;

// changed from ArrayList for recipes param
    public RecipeCardAdapter(Context context, List<Recipes> recipes) {
        super();
        this.mContext = context;
        this.mRecipes = new ArrayList<Recipes>(recipes);
        inflater = LayoutInflater.from(context);
    }


    // getCount() very important, it is called and size of ImageAdapter ArrayList checked, if 0
    // the RecyclerView will not create any ViewHolders
    public int getCount() {
        return mRecipes.size();
    }

    public void clear() { mRecipes.clear(); }
    public Recipes getItem(int position) {
        return mRecipes.get(position);
    }
    public long getItemId(int position) {
        return 0;
    }


    // Sets list of movies to the adapter
    public void setList(List<Recipes> recipes)
    {
        if(recipes != null){
            for(Recipes movieObj : recipes) {
                mRecipes.add(movieObj);
            }
            this.notifyDataSetChanged();
        }
    }




    // Convert Recipe to TestRecipe container for now
/*
    public void setList(List<Recipes> movies)
    {
        if(movies != null){
            for(Recipes movieObj : movies) {
                TestIngredient i = new TestIngredient("", "", "", null);
                ArrayList<TestIngredient> ii = new ArrayList<TestIngredient>();
                ii.add(i);

                TestStep s = new TestStep("", "", "", "", "");
                ArrayList<TestStep> ss = new ArrayList<TestStep>();
                ss.add(s);

                TestRecipe r = new TestRecipe(
                        movieObj.getR_id(),
                        movieObj.getName(),
                        ii, ss,
                        movieObj.getServings(),
                        movieObj.getImage()
                );
                mRecipes.add(r);
            }
            this.notifyDataSetChanged();
        }
    }
*/

    // Add MovieRoom entity to adapter
    public void add(Recipes r) {
        mRecipes.add(r);
        notifyDataSetChanged();
    }


    // Constructor
    public RecipeCardAdapter(Context c) {
        mContext = c;
    }


    //////////////////////////////////////////////////////////////////////////
    // MovieViewHolder class
    // ViewHolder is an item view which is an item in the RecyclerView
    // findViewById() calls done here and one time instead of many calls
    @Override
    public RecipeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Initialize the specific view for this MovieViewHolder
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeCardViewHolder viewHolder = new RecipeCardViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecipeCardViewHolder holder, int position) {
        //Bind each item to the correspond views
        holder.bind(mRecipes.get(position));
    }


    // this gets called right after MainActivity onCreate() and GridLayout Manager is created
    // RecyclerView instantiated and triggered this to get size
    @Override
    public int getItemCount()
    {
        return mRecipes != null ? mRecipes.size(): 0;
    }



    /*
        Views/items in the list are represented by ViewHolder objects which extend from RecyclerView.ViewHolder
     */
    public class RecipeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mRecipeThumbnail;
        public TextView mRecipeName;
        public TextView mRecipeServings;


        // Constructor: Assign reference to grid element and set a click listener to it
        RecipeCardViewHolder(View itemView) {
            super(itemView);
            mRecipeThumbnail = itemView.findViewById(R.id.recipe_image);
            mRecipeThumbnail.setOnClickListener(this);
            mRecipeName = itemView.findViewById(R.id.recipe_name);
            mRecipeServings = itemView.findViewById(R.id.recipe_servings);

        }


        // Bind the ViewHolder to Adapter content by assigning adapter movie data to the movie
        // thumbnail UI element reference
        void bind(Recipes recipe) {
            //Use Picasso to load each image
            // load from adapter variable the particular movie
            //Picasso.get().setLoggingEnabled(true);
            // when i changed http to https, then the image loaded
            //Picasso.get().load("https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg").into(mRecipeThumbnail);
            //String movie_url = "https://image.tmdb.org/t/p/w185/" + movie.getPoster_path();
            //Picasso.get().load(movie_url).into(mRecipeThumbnail);
            //https://github.com/square/picasso/issues/427
            String url = "https://image.tmdb.org/t/p/w185//xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg";
            Glide.with(itemView)  //2
                    .load(url) //3
                    .centerCrop() //
                    .into(mRecipeThumbnail); //8
            /*
            .placeholder(R.drawable.ic_image_place_holder) //5
                    .error(R.drawable.ic_broken_image) //6
                    .fallback(R.drawable.ic_no_image) //7
             */

            mRecipeName.setText(recipe.getName());
            mRecipeServings.setText(recipe.getServings());


            //https://stackoverflow.com/questions/51974756/android-viewmodel-inside-recyclerview-adapter-for-lazy-database-downloads

            // Get recipe card data via ViewModel

        }


        @Override
        public void onClick(View v) {

            //https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
            //We need to send the proper url for the image so that when the DetailActivity opens it has the proper image url

            //Send now the movie object with the proper movie poster url
            //mMovieItemClicked.onMovieItemClicked(mMovies.get(getAdapterPosition()), this);

            Intent i = new Intent(mContext, RecipeActivity.class);
            Recipes m = mRecipes.get(getAdapterPosition());

            // Best practice design is to pass the id of the object we are navigating to instead
            // of the whole object which is expensive
            Intent id = i.putExtra("id", m.getId());
            mContext.startActivity(i);


        }
    }
}

/*
If you are interested there is also anther library called Glide which can compete with Picasso in certain aspects:

Glide ➡️ https://github.com/codepath/android_guides/wiki/Displaying-Images-with-the-Glide-Library
Picasso vs Glide ➡️ https://yourwebsitefirst.com/picasso-vs-glide-advantage-disadvantage/

 */

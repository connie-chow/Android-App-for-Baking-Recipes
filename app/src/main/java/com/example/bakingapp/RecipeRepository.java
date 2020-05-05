package com.example.bakingapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bakingapp.model.Feed;
import com.example.bakingapp.model.ModelIngredients;
import com.example.bakingapp.model.ModelSteps;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.bakingapp.RedditAPI.BASE_URL;

public class RecipeRepository {

    private static RecipeRepository instance;
    private ArrayList<TestRecipe> dataSet = new ArrayList();
    private Context appContext;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private AppDatabase mDb;
    private LiveData<List<Recipes>> mAllRecipes;
    private LiveData<List<Steps>> mRecipeSteps;
    private LiveData<List<Ingredients>> mRecipeIngredients;
    private LiveData<Steps> mRecipeStepDetails;


    // Singleton pattern so as to avoid a bunch of open connections to your webservice
    public static RecipeRepository getInstance(Application application) {
        if(instance == null) {
            instance = new RecipeRepository(application);
        }
        return instance;
    }


    public RecipeRepository(Application application) {
        mDb = AppDatabase.getInstance(application.getApplicationContext());
        mAllRecipes = mDb.recipeDAO().getAllRecipes();
    }


    LiveData<List<Recipes>> getAllRecipes() {
        return mAllRecipes;
    }

    LiveData<List<Steps>> getRecipeSteps(String recipe_id) {
        mRecipeSteps = mDb.recipeDAO().getRecipeSteps(recipe_id);
        return mRecipeSteps;
    }

    LiveData<List<Ingredients>> getRecipeIngredients(String recipe_id) {
        mRecipeIngredients = mDb.recipeDAO().getRecipeIngredients(recipe_id);
        return mRecipeIngredients;
    }


    public void deleteAll() {
        mDb.recipeDAO().deleteAll();
    }


    LiveData<Steps> getRecipeStepDetails(String recipe_id, String step_id) {
        mRecipeStepDetails = mDb.recipeDAO().getRecipeStepDetails(recipe_id, step_id);
        return mRecipeStepDetails;
    }


    // Fetch retrieved data
    public MutableLiveData<List<TestRecipe>> getRecipes(Context c) {
        setRecipes(c);
        MutableLiveData<List<TestRecipe>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }


    // Fetch data from online source/webservice
    private void setRecipes(Context c) {

        // Fetch JSON data and then write to Room Database, then write to dataSet to send back to
        // ViewModel
        // Retrofit2
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call<List<Feed>> call = redditAPI.loadRecipeData();
        AppDatabase mDb = AppDatabase.getInstance(c);


        call.enqueue(new Callback<List<Feed>>() {
            @Override
            public void onResponse(Call <List<Feed>> call, Response<List<Feed>> response) {
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                Log.d(TAG, "onResponse: received information: " + response.body().toString());

                // testing////////////////////////////////////////////////////////////
                //List<Recipes> totalRecipesList_test = (List<Recipes>) mDb.recipeDAO().getAllRecipes();
                //List<Ingredients> totalIngredientsList_test = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                //Log.d(LOG_TAG, "totalIngredientsList.size(): " + totalIngredientsList_test.size());


                // response body is an ArrayList
                String result = response.body().toString();
                Feed recipe;
                // get the parsed data and insert into Room
                for(int i = 0; i< response.body().size(); i++ ) {
                    recipe = response.body().get(i);


                    // Recipe Data
                    String name = recipe.getName();
                    String servings = recipe.getServings();
                    String image = recipe.getImage();
                    String recipeId = recipe.getId();

                    Recipes recipe_card = new Recipes(recipeId, name, servings, image);

                    long room_id = mDb.recipeDAO().insertRecipe(recipe_card);


                    // Recipe Ingredients
                    ArrayList ingredientsList = recipe.getIngredients();
                    String recipe_id = recipe.getId();
                    ModelIngredients item = null;
                    Ingredients room_ingredients;

                    for(int j = 0; j< ingredientsList.size(); j++) {
                        item = (ModelIngredients)ingredientsList.get(j);
                        String id = item.getId();
                        String text = item.getIngredient();
                        String measure = item.getMeasure();
                        String quantity = item.getQuantity();
                        room_ingredients =  new Ingredients(
                                recipe_id, Integer.toString(j), quantity, measure, text
                        );
                        long room_insert = mDb.recipeDAO().insertIngredients(room_ingredients);
                        Log.d(LOG_TAG, "recipe_id = " + recipe_id + "ingredient_id = " + j);
                        Log.d(LOG_TAG, "insert return code: " + room_insert);
                        List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                        Log.d(LOG_TAG, "totalIngredientsList.size(): " + totalIngredientsList.size());
                    }
                    List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                    Log.d(LOG_TAG, "FINAL totalIngredientsList.size(): " + totalIngredientsList.size());


                    ArrayList stepsList = recipe.getSteps();
                    ModelSteps steps = null;
                    Steps room_step;

                    for(int k = 0; k < stepsList.size(); k++) {
                        steps = (ModelSteps)stepsList.get(k);
                        String id = steps.getId();
                        String shortDescription = steps.getShortDescription();
                        String description = steps.getDescription();
                        String videoURL = steps.getVideoURL();
                        String thumbnailURL = steps.getThumbnailURL();

                        room_step = new Steps(
                                recipe_id, id, shortDescription, description, videoURL, thumbnailURL
                        );


                        long room_insert_step = mDb.recipeDAO().insertSteps(room_step);
                        Log.d(LOG_TAG, "recipe_id = " + recipe_id + "step_id = " + id);
                        Log.d(LOG_TAG, "insert return code: " + room_insert_step);
                        List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                        Log.d(LOG_TAG, "totalStepsList.size(): " + totalStepsList.size());
                    }
                    List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                    Log.d(LOG_TAG, "totalStepsList.size(): " + totalStepsList.size());

                    // pass to adapter
                    List<Recipes> totalRecipesList = (List<Recipes>) mDb.recipeDAO().getAllRecipes2();
                    TestIngredient i0 = new TestIngredient(
                            "0", "2", "CUP", "Graham Cracker crumbs"
                    );
                    ArrayList<TestIngredient> ingredients = new ArrayList<TestIngredient>();
                    ingredients.add(i0);
                    TestStep s0 = new TestStep(
                            "0", "Recipe Introduction", "Recipe Introduction",
                            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                            ""
                    );
                    ArrayList<TestStep> s = new ArrayList<TestStep>();
                    s.add(s0);
                    TestRecipe recipe1 = new TestRecipe(
                            "1",
                            "Nutella Pie",
                            ingredients,
                            s,
                            "8",
                            ""
                    );
                    dataSet.add(recipe1);
                    //data.setValue(dataSet);



                }

                //List<Recipes> totalRecipesList = (List<Recipes>) mDb.recipeDAO().getAllRecipes();
                //List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                //List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                //List<Feed> childrenList = response.body().loadRecipeData();
                /*
                for (int i = 0; i < childrenList.size(); i++) {
                    Log.d(TAG, "onResponse: \n" +
                            "kind: " + childrenList.get(i).getKind() + "\n");
                }
                */


                // now read from Room and set to dataSet


            }

            @Override
            public void onFailure(Call <List<Feed>> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage());
                //Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        });



/*
        TestIngredient i0 = new TestIngredient(
                "0", "2", "CUP", "Graham Cracker crumbs"
        );
        TestIngredient i1 = new TestIngredient(
                "0", "6", "TBLSP", "unsalted butter, melted"
        );
        TestIngredient i2 = new TestIngredient(
                "0", "0.5", "CUP", "granulated sugar"
        );
        TestIngredient i3 = new TestIngredient(
                "0", "1.5", "TSP", "salt"
        );
        TestIngredient i4 = new TestIngredient(
                "0", "5", "TBLSP", "vanilla"
        );
        TestIngredient i5 = new TestIngredient(
                "0", "1", "K", "Nutella or other chocolate-hazelnut spread"
        );
        TestIngredient i6 = new TestIngredient(
                "0", "500", "G", "Mascapone Cheese(room temperature)"
        );
        TestIngredient i7 = new TestIngredient(
                "0", "1", "CUP", "heavy cream(cold)"
        );
        TestIngredient i8 = new TestIngredient(
                "0", "4", "OZ", "cream cheese(softened)"
        );

        ArrayList<TestIngredient> ingredients = new ArrayList<TestIngredient>();
        ingredients.add(i0);
        ingredients.add(i1);
        ingredients.add(i2);
        ingredients.add(i3);
        ingredients.add(i4);
        ingredients.add(i5);
        ingredients.add(i6);
        ingredients.add(i7);
        ingredients.add(i8);

        TestStep s0 = new TestStep(
                "0", "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                ""
        );
        TestStep s1 = new TestStep(
                "1", "Starting prep", "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                "",
                ""
        );
        TestStep s2 = new TestStep(
                "2", "Prep the cookie crust.", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4",
                ""
        );
        TestStep s3 = new TestStep(
                "3", "Press the crust into baking form.", "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4",
                ""
        );
        TestStep s4 = new TestStep(
                "4", "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                ""
        );
        TestStep s5 = new TestStep(
                "5", "Recipe Introduction", "Recipe Introduction",
                "",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"
        );
        TestStep s6 = new TestStep(
                "6", "Recipe Introduction", "Recipe Introduction",
                "",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"
        );

        ArrayList<TestStep> s = new ArrayList<TestStep>();
        s.add(s0);
        s.add(s1);
        s.add(s2);
        s.add(s3);
        s.add(s4);
        s.add(s5);
        s.add(s6);

        // https://www.raywenderlich.com/2945946-glide-tutorial-for-android-getting-started

        TestRecipe recipe = new TestRecipe(
                "1",
                "Nutella Pie",
                ingredients,
                s,
                "8",
                ""
        );
        /// END RECIPE 1 //////////////////////////////////////////////////////////////
        // RECIPE 2 ///////////////////////////////////////////////////////////////////
        TestRecipe recipe2 = new TestRecipe(
                "1",
                "Peach Pie",
                ingredients,
                s,
                "4",
                ""
        );

        dataSet.add(recipe);
        dataSet.add(recipe2);

 */
    }

}

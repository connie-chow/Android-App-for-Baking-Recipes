package com.example.bakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.bakingapp.model.Feed;
import com.example.bakingapp.model.ModelIngredients;
import com.example.bakingapp.model.ModelSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.bakingapp.RedditAPI.BASE_URL;


// Main Screen containing the Recipe Cards
// Tablet has 3x3 Recipe Card layout
// Mobile has 1xN Recipe Card Layout
// Ideally we use a Fragment to hold the Recipe Cards also but to simplify things since
// it's my first app, I'm not going to do it
public class MainActivity extends AppCompatActivity {

    RecipeCardAdapter mRecipeCardAdapter;
    private AppDatabase mDb;
    private MainViewModel mViewModel;
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // clear for testing////////////////////////////////////////////////////////////////////
        /*
        mDb = AppDatabase.getInstance(getApplicationContext());
        mDb.recipeDAO().deleteAll();
        mDb.recipeDAO().deleteAllIngredients();
        mDb.recipeDAO().deleteALLSteps();
        List<Recipes> totalRecipesList = (List<Recipes>) mDb.recipeDAO().getAllRecipes2();
        List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
        Log.d(LOG_TAG, "totalIngredientsList.size(): " + totalIngredientsList.size());

        // ViewModel data should be fetched here and then used to populate the adapter/view
        mViewModel = ViewModelProviders.of(this,
                new Factory(this.getApplication()))
                .get(MainViewModel.class);
        mViewModel.init();
        mViewModel.getRecipes().observe(this, new Observer<List<Recipes>>() {
            @Override
            public void onChanged(@Nullable List<Recipes> newRecipes) {
                mRecipeCardAdapter.notifyDataSetChanged();
            }
        });
*/
        String s = getScreenResolution(getApplicationContext());
        //https://stackoverflow.com/questions/4605527/converting-pixels-to-dp

        RecyclerView gridView = (RecyclerView) findViewById(R.id.rv_recipe_cards);

        // detect if mobile or tablet based on dpi, do the span in the xml file
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        mRecipeCardAdapter = new RecipeCardAdapter(this, new ArrayList<Recipes>());
        //mRecipeCardAdapter = new RecipeCardAdapter(this, mViewModel.getRecipes().getValue());

        gridView.setAdapter(mRecipeCardAdapter);
/*
        FetchRecipesTask recipes = new FetchRecipesTask();
        recipes.execute();
*/

        // Retrofit2
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedditAPI redditAPI = retrofit.create(RedditAPI.class);
        Call <List<Feed>> call = redditAPI.loadRecipeData();
        mDb = AppDatabase.getInstance(getApplicationContext());
        mDb.recipeDAO().deleteAllIngredients();
        //mDb.recipeDAO().deleteAllSteps();
        mDb.recipeDAO().deleteAll();

        List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
        Log.d(LOG_TAG, "totalIngredientsList.size(): " + totalIngredientsList.size());


        //https://stackoverflow.com/questions/24154917/retrofit-expected-begin-object-but-was-begin-array

        call.enqueue(new Callback <List<Feed>>() {
            @Override
            public void onResponse(Call <List<Feed>> call, Response <List<Feed>> response) {

                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                Log.d(TAG, "onResponse: received information: " + response.body().toString());

                // response body is an ArrayList
                ArrayList<Recipes> recipeList = new ArrayList<Recipes>();
                String result = response.body().toString();
                Feed recipe;


                // get the parsed data and insert into Room, looping on each Recipe Card
                for(int i = 0; i< response.body().size(); i++ ) {

                    recipe = response.body().get(i);

                    // Get Recipe Data
                    String name = recipe.getName();
                    String servings = recipe.getServings();
                    String image = recipe.getImage();
                    String recipeId = recipe.getId();


                    // Get Recipe Ingredients
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

                        long insertIngredientsReturnCode = mDb.recipeDAO().insertIngredients(room_ingredients);
                        Log.d(LOG_TAG, "recipe_id = " + recipe_id + " ingredient_id = " + j);
                        Log.d(LOG_TAG, "insert Ingredient return code: " + insertIngredientsReturnCode);
                        List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                        Log.d(LOG_TAG, "totalIngredientsList.size(): " + totalIngredientsList.size());
                    }

                    List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                    Log.d(LOG_TAG, "FINAL totalIngredientsList.size(): " + totalIngredientsList.size());


                    // Get Recipe Steps
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


                        long insertStepReturnCode = mDb.recipeDAO().insertSteps(room_step);
                        Log.d(LOG_TAG, "recipe_id = " + recipe_id + " step_id = " + id);
                        Log.d(LOG_TAG, "insert return code: " + insertStepReturnCode);
                        List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                        Log.d(LOG_TAG, "totalStepsList.size(): " + totalStepsList.size());
                    }

                    List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                    Log.d(LOG_TAG, "FINAL totalStepsList.size(): " + totalStepsList.size());

                    // pass to adapter
                   // List<Recipes> totalRecipesList = (List<Recipes>) mDb.recipeDAO().getAllRecipes();

                    Recipes recipe_card = new Recipes(recipeId, name, servings, image);
                    long insertRecipeReturnCode = mDb.recipeDAO().insertRecipe(recipe_card);
                    Log.d(LOG_TAG, "insert Recipe return code: " + insertRecipeReturnCode);

                    recipeList.add(recipe_card);
                }

                mRecipeCardAdapter.setList(recipeList);
                mRecipeCardAdapter.notifyDataSetChanged();

                //List<Recipes> totalRecipesList = (List<Recipes>) mDb.recipeDAO().getAllRecipes();
                //List<Ingredients> totalIngredientsList = (List<Ingredients>) mDb.recipeDAO().getAllIngredients();
                //List<Steps> totalStepsList = (List<Steps>) mDb.recipeDAO().getAllSteps();
                //List<Feed> childrenList = response.body().loadRecipeData();
            }

            @Override
            public void onFailure(Call <List<Feed>> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }




//https://stuff.mit.edu/afs/sipb/project/android/docs/training/multiscreen/screensizes.html
//http://www.appstoremarketresearch.com/articles/android-tutorial-master-detail-flow/
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private static String getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        float width_f = convertPixelsToDp(width, context);
        float height_f = convertPixelsToDp(height, context);
        String width_s = String.valueOf(width_f);
        String height_s = String.valueOf(height_f);

        return "{" + width_s + "," + height_s + "}";
    }

    // Why do we create the AsyncTask nested inside the MainActivity?
    public class FetchRecipesTask extends AsyncTask<String, Void, ArrayList<TestRecipe>> {

        ProgressDialog progressBar;

        @Override
        protected ArrayList<TestRecipe> doInBackground(String... params)
        {

            //https://stackoverflow.com/questions/20239386/how-to-parse-data-from-2-different-urls-by-asynctask-method
            //7ce66efa6f9aed94ee0c073d4e188678 API KEY from movie db
            //http://api.themoviedb.org/3/movie/popular?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //http://api.themoviedb.org/3/movie/top_rated?api_key=7ce66efa6f9aed94ee0c073d4e188678
            //android.os.Debug.waitForDebugger();
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0)
            {
                return null;
            }

            //https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
            ArrayList<TestRecipe> recipesList = new ArrayList<TestRecipe>();

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String recipesJsonStr = null;
            Uri builtUri;
            int page = 1;

            while(page < 5) {

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are avaiable at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    // why did adding an s to http work? to get connection
                    final String RECIPE_LIST_URL =
                            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

                    builtUri = Uri.parse(RECIPE_LIST_URL).buildUpon().build();
                    URL url = new URL(builtUri.toString());

                    //Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                    // Create the request and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    // why did connect() work after uninstalling app in emulator and then reinstalling
                    // and then adding network security to androidmanifest?
                    //https://developer.android.com/training/basics/network-ops/connecting
                    // https://stackoverflow.com/questions/56266801/java-net-socketexception-socket-failed-eperm-operation-not-permitted
                    // https://stackoverflow.com/questions/31762955/asynctask-permission-denied-missing-internet-permission
                    //https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted
                    urlConnection.connect();
                    int responsecode = urlConnection.getResponseCode();

                    // Keep requesting additional pages of data until unable to get anymore data
                    // 200 = success response
                    if (responsecode != 200) {
                        break;
                    }

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    int s = inputStream.available();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    recipesJsonStr = buffer.toString();
                    //Log.v(LOG_TAG, "Forecast string: " + movieJsonStr);

                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the data, there's no point in attemping
                    // to parse it.
                    return null;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            // Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }

                try {
                    recipesList.addAll(getRecipeDataFromJson(recipesJsonStr));
                    page++;
                } catch (JSONException e) {
                    // Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            } // while

            return recipesList;
        }



        /**
         * Take the String representing the requested movies in JSON Format and
         * insert the data into Room DB
         */
        private ArrayList<TestRecipe> getRecipeDataFromJson(String recipeJsonStr)
                throws JSONException {

            final String OWM_RESULTS = "results";
            final String OWM_NAME = "name";
            final String OWM_INGREDIENTS = "ingredients";
            final String OWM_QUANTITY = "quantity";
            final String OWM_MEASURE = "measure";
            final String OWM_INGREDIENT = "ingredient";
            final String OWM_STEPS = "steps";
            final String OWM_SHORT_DESCRIPTION = "shortDescription";
            final String OWM_DESCRIPTION = "description";
            final String OWM_VIDEO_URL = "videoURL";
            final String OWM_THUMBNAIL_URL = "thumbnailURL";
            final String OWM_SERVINGS = "servings";
            final String OWM_IMAGE = "image";


            ArrayList<TestRecipe> resultList = new ArrayList<TestRecipe>();

            /*
            String poster_path, adult, overview, release_date, genre_ids, id, original_title, original_language, title, backdrop_path, popularity, vote_count, video, vote_average;

            JSONObject movieJson = new JSONObject(recipeJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

            for (int i = 0; i < movieArray.length(); i++) {

                // Get the JSON object representing the movie
                JSONObject movie = movieArray.getJSONObject(i);

                results = movie.getString(OWM_RESULTS); //.getJSONObject(0);
                name = movie.getString(OWM_NAME);
                ingredients = movie.getString(OWM_INGREDIENTS);
                quantity = movie.getString(OWM_QUANTITY);
                genre_ids = movie.getString(OWM_MEASURE);
                id = movie.getString(OWM_STEPS);
                original_title = movie.getString(OWM_SHORT_DESCRIPTION);
                original_language = movie.getString(OWM_DESCRIPTION);
                title = movie.getString(OWM_VIDEO_URL);
                backdrop_path = movie.getString(OWM_THUMBNAIL_URL);
                popularity = movie.getString(OWM_SERVINGS);
                vote_count = movie.getString(OWM_IMAGE);


                // Write JSON data to Movie Room DB
                MovieRoom m = new MovieRoom(
                        poster_path,
                        adult,
                        overview,
                        release_date,
                        genre_ids,
                        id,
                        original_title,
                        original_language,
                        title,
                        backdrop_path,
                        popularity,
                        vote_count,
                        video,
                        vote_average);

                // check if already in db (preserve favorite flag), else insert
                //MovieRoom db_entry = mDb.movieDao().loadMovieByMId(m.getM_id());
                long room_id = mDb.movieDao().insertMovie(m);

                // pass to adapter as String array....in onPostExecute
                resultList.add(m);
            }
            //List<MovieRoom> movies_after = mDb.movieDao().getAllMovies();  //340

             */



            return resultList;

        }



        @Override
        protected void onPreExecute() {
           // progressBar = new ProgressDialog(MainActivity.this);
           // progressBar.setMessage("Downloading...");
           // progressBar.show();
        }

        @Override
        protected void onPostExecute(ArrayList<TestRecipe> result) {
            // string result which came from the doinbackground()
            // take result and add each movie to the adapter
            //android.os.Debug.waitForDebugger();

            // Insert some test data, we do AsyncTask JSON fetch later with Retrofit
            // RECIPE 1 ///////////////////////////////////////////////////////////////
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
            // END RECIPE 2 //////////////////////////////////////////////////////////////////////

                mRecipeCardAdapter.clear();
                mRecipeCardAdapter.add(recipe);
                mRecipeCardAdapter.add(recipe2);
                mRecipeCardAdapter.notifyDataSetChanged();
*/
            }
            /*
            if(result != null) {
                mMovieAdapter.setList(result);
            }
             */


/*
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
            */

    } //asynctask

}

/*
https://github.com/udacity/Android_Me/tree/TFragments.04-Exercise-CreateMasterListFragment/app/src/main/java/com/example/android/android_me/ui
https://classroom.udacity.com/nanodegrees/nd801/parts/ec45ffe9-2c4e-4b8d-ad76-d80c5905d926/modules/2ad3410b-6505-418c-9b74-2d4f67880313/lessons/79936377-aa69-45ae-a386-d865dcb36518/concepts/c2fa39f7-e4ed-4340-97dc-351dc7b84fbe

 */
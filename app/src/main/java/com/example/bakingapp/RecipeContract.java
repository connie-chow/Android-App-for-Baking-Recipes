package com.example.bakingapp;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.bakingapp";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "plants" directory
    public static final String PATH_RECIPES = "recipes";

    public static final long INVALID_PLANT_ID = -1;

    public static final class RecipeEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_ID = "r_id";
        public static final String COLUMN_RECIPE_NAME = "name";
        //public static final String COLUMN_CREATION_TIME = "createdAt";
        //public static final String COLUMN_LAST_WATERED_TIME = "lastWateredAt";
    }
}
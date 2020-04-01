package com.example.bakingapp;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import static javax.xml.transform.OutputKeys.VERSION;

// This is your app database class where the singleton instance of the database is instantiated

@Database(
        entities = {
                Recipes.class,
                Ingredients.class,
                Steps.class
        },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDAO recipeDAO();

    private static volatile AppDatabase INSTANCE;
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "recipes";
    private static final Object LOCK = new Object();
//https://stackoverflow.com/questions/57473172/android-room-one-database-with-multiple-tables
    public static AppDatabase getInstance(Context context)
    {
        // Singleton pattern, we always use the same instance originally created
        if (INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if(INSTANCE == null)
                {
                    Log.d(LOG_TAG, "Creating new database instance");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "maindb")
                            .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
                    //.build();
                    // .allowMainThreadQueries().build();
                    // Queries should be done on separate thread, we allow this temporarily to
                    // check that the database is working
                }
                Log.d(LOG_TAG, "Getting the database instance");
            }
        }
        return INSTANCE;
    }

}

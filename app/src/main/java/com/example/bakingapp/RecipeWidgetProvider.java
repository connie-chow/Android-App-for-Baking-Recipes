package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
//ArrayList<Map<String, String>> recipeList,
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<Map<String, String>> recipes,
                                int[] appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // create TextView element
        for (Map<String, String> recipe : recipes) {
            String recipe_name = recipe.get("recipe_name");
            //views.setTextViewText(R.id.appwidget_text, recipe_name);
            RemoteViews r = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            r.setTextViewText(R.layout.recipe_widget, recipe_name);
            views.addView(R.layout.recipe_widget, r);

        }



/* Launch MainActivity onClick()
        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_recipe_image, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
*/


        // Add the wateringservice click handler
        Intent wateringIntent = new Intent(context, RecipeFetchService.class);
        wateringIntent.setAction(RecipeFetchService.ACTION_FETCH_RECIPES);
        PendingIntent wateringPendingIntent = PendingIntent.getService(
                context,
                0,
                wateringIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_image, wateringPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }


    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, ArrayList<Map<String, String>> recipeList, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeList, appWidgetIds);
            //updateAppWidget(context, appWidgetManager, appWidgetIds);
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        /*
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetIds);
        }
                 */
        RecipeFetchService.startActionUpdatePlantWidgets(context);
    }




    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.e(TAG, "RecipeWidgetProvider: onEnabled()");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.e(TAG, "RecipeWidgetProvider: onDisabled()");
    }
}


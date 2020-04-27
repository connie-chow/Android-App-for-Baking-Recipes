package com.example.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
                                int appWidgetId) {

        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        RemoteViews rv;
        if(width < 100) {
            rv = getSinglePlantRemoteView(context, 0,0,true);
        } else {
            rv = getGardenGridRemoteView(context);
        }
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, ArrayList<Map<String, String>> recipes,
                                int[] appWidgetId) {


        //CharSequence widgetText = context.getString(R.string.appwidget_text);

        Intent intent = new Intent(context, RecipeFetchService.class);
        //intent.setAction(RecipeFetchService.ACTION_FETCH_RECIPES);

        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                2,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setOnClickPendingIntent(R.id.android_me_linear_layout, pendingIntent);

        // create TextView element
        String recipe_str = "", recipe_key = "";
        for (Map<String, String> recipe : recipes) {

            for (Map.Entry<String,String> entry : recipe.entrySet()) {
                recipe_str = entry.getValue();
                recipe_key = entry.getKey();
            }

            CharSequence recipe_name = new StringBuilder(recipe_str);
            views.setTextViewText(R.id.appwidget_text, recipe_name);
            // only lodas nutella string after add new widget
            //RemoteViews r = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
            //r.setTextViewText(R.layout.recipe_widget, recipe_name);
            //views.addView(R.layout.recipe_widget, r);




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

        /* onclick events in Remove Views done via PendingIntents
        Pending Intents: Wrapper for Intent and allows external applications to have access to that
        intent and run it in your application
        So you create an Intent to launch the MainActivity and then wrap it with a PendingIntent
         */

        // Add the wateringservice click handler
/*
        Intent wateringIntent2 = new Intent(context, RecipeFetchService.class);
        wateringIntent2.setAction(RecipeFetchService.ACTION_FETCH_RECIPES);
        wateringIntent2.setAction("STOP");
        PendingIntent wateringPendingIntent = PendingIntent.getService(
                context,
                1,
                wateringIntent2,
                PendingIntent.FLAG_CANCEL_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_image, wateringPendingIntent);
        //appWidgetManager.updateAppWidget(appWidgetId, views);
*/
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //https://stackoverflow.com/questions/29519632/android-widget-onclick-not-working-service-wont-start


    }


    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, ArrayList<Map<String, String>> recipeList, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            //updateAppWidget(context, appWidgetManager, recipeList, appWidgetIds);
            updateAppWidget(context, appWidgetManager, recipeList, appWidgetId);
            //updateAppWidget(context, appWidgetManager, appWidgetIds);
        }
    }


    /*
    Called whenever the widget is installed and then called on every widget update interval,
    interval is set in the widget_provider.xml
    Goes through all widgets that have been added to the homescreen and updates them
    AppWidgetManager gives access to all existing widgets on the homescreen
    and access to update them
     */
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



    private static RemoteViews getSinglePlantRemoteView(Context context, int imgRes, long plantId, boolean showWater) {
        Intent intent;
        intent = new Intent(context, MainActivity.class);
        //intent = new Intent(context, RecipeDetailActivity.class);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        return views;
    }


    private static RemoteViews getGardenGridRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }


    /*
    Called everytime dimension of the widget is changed
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeFetchService.startActionUpdatePlantWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

// https://github.com/android/architecture-components-samples/tree/master/PersistenceContentProviderSample/app/src/main
// Room database reading with content provider
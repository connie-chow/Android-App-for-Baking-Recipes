package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.RecipeContentProvider;

import static com.example.bakingapp.RecipeContract.BASE_CONTENT_URI;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}


class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }


    @Override
    public void onCreate() {
        // https://stackoverflow.com/questions/24603838/gridview-in-android-widgets


    }


    // called on start and when onNotifyAppWidgetViewDataChanged  is called
    @Override
    public void onDataSetChanged() {
        Uri RECIPES_URI = RecipeContentProvider.URI_RECIPE;

        //Uri URL = BASE_CONTENT_URI.buildUpon().appendPath(String.valueOf(RecipeContentProvider.URI_RECIPE)).build();
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = mContext.getContentResolver().query(
                RECIPES_URI,
                null,
                null,
                null,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID
        );
        //mCursor = mContext.getContentResolver().query(RECIPES_URI, null, null, null, null);
    }


    @Override
    public void onDestroy() {
        mCursor.close();
    }


    @Override
    public int getCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }


    @Override
    public RemoteViews getViewAt(int position) {

        if(mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int recipe_id = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID);
        int recipe_name = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_NAME);
        final String name = mCursor.getString(recipe_name);
        final String id = mCursor.getString(recipe_id);

        //int idIndex = mCursor.getColumnIndex("name");
        //long plantId = mCursor.getLong(idIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

        // update plant image
        //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
        //views.setImageViewResource(R.id.widget_plant_image, imgRes);
        //views.setTextViewText(R.id.appwidget_text, String.valueOf(plantId));
        views.setTextViewText(R.id.appwidget_text, name);
        views.setViewVisibility(R.id.widget_recipe_image, View.GONE);
        //views.setVisibility(R.id.widget_water_button, View.GONE);

        // fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        // fill with recipe ID to retrieve the ingredient
        Bundle extras = new Bundle();
        extras.putString("id", id);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.appwidget_text, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {

        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

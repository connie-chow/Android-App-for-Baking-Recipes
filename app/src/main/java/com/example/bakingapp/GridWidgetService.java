package com.example.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

    }


    // called on start and when onNotifyAppWidgetViewDataChanged  is called
    @Override
    public void onDataSetChanged() {
        Uri RECIPES_URI = RecipeContentProvider.URI_RECIPE;

        //Uri URL = BASE_CONTENT_URI.buildUpon().appendPath(String.valueOf(RecipeContentProvider.URI_RECIPE)).build();
        if (mCursor != null) {
            mCursor.close();
        }

        Cursor cursor = mContext.getContentResolver().query(
                RECIPES_URI,
                null,
                null,
                null,
                RecipeContract.RecipeEntry.COLUMN_RECIPE_ID
        );
        //mCursor = mContext.getContentResolver().query(RECIPES_URI, null, null, null, null);
    }


    @Override
    public void onDestroy() { mCursor.close(); }


    @Override
    public int getCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }


    @Override
    public RemoteViews getViewAt(int position) {
        if(mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex("recipe_name");

        long plantId = mCursor.getLong(idIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

        // update plant image
        //int imgRes = PlantUtils.getPlantImageRes(mContext, timeNow - createdAt, timeNow - wateredAt, plantType);
        //views.setImageViewResource(R.id.widget_plant_image, imgRes);
        views.setTextViewText(R.id.appwidget_text, String.valueOf(plantId));
        //views.setVisibility(R.id.widget_water_button, View.GONE);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

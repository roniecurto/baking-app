package com.example.baking_app.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.baking_app.R;
import com.example.baking_app.provider.IngredientContract;

import static com.example.baking_app.provider.IngredientContract.BASE_CONTENT_URI;
import static com.example.baking_app.provider.IngredientContract.PATH_INGREDIENTS;

public class GridViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridViewRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;

    public GridViewRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all ingredients
        Uri INGREDIENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
        if (mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(INGREDIENT_URI, null, null, null,null);
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the ListView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int ingredientDescriptionIndex = mCursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_INGREDIENT_DESCRIPTION);
        int recipeIndex = mCursor.getColumnIndex(IngredientContract.IngredientEntry.COLUMN_RECIPE);
        String ingredientDescription = mCursor.getString(ingredientDescriptionIndex);
        String recipeName = mCursor.getString(recipeIndex);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list);
        views.setTextViewText(R.id.ingredient_line, ingredientDescription);
        views.setTextViewText(R.id.appwidget_recipe_name, recipeName);

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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

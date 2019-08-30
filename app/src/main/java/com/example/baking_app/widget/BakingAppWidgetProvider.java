package com.example.baking_app.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.baking_app.ItemDetailActivity;
import com.example.baking_app.ItemListActivity;
import com.example.baking_app.R;
import com.example.baking_app.fragments.RecipeDetailFragment;
import com.example.baking_app.models.Recipe;


/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static Recipe recipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = getGridViewRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Creates and returns the RemoteViews to be displayed in the GridView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the GridView mode widget
     */
    private static RemoteViews getGridViewRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridViewWidgetService.class);
        views.setRemoteAdapter(R.id.appwidget_ingredients_list, intent);

        // Set the ItemDetailActivity intent to launch when clicked
        Intent appIntent;
        if(recipe == null){
            appIntent = new Intent(context, ItemListActivity.class);
        }else{
            appIntent = new Intent(context, ItemDetailActivity.class);
            appIntent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, recipe);
        }

        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_ingredients_list, appPendingIntent);
        views.setEmptyView(R.id.appwidget_ingredients_list, R.id.empty_view);
        return views;
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeIngredientsService.startActionUpdateWidget(context, recipe);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipeSelected) {
        recipe = recipeSelected;

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeIngredientsService.startActionUpdateWidget(context, recipe);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }
}


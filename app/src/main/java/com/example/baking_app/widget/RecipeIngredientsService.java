package com.example.baking_app.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.example.baking_app.R;
import com.example.baking_app.models.Recipe;

public class RecipeIngredientsService extends IntentService {
    private static final String ACTION_UPDATE_WIDGET = "com.example.baking_app.widget.action.update_widget";
    private static final String ACTION_UPDATE_WIDGET_PARAM = "recipe";

    public RecipeIngredientsService() {
        super("RecipeIngredientsService");
    }

    /**
     * Starts this service to perform action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateWidget(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeIngredientsService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(ACTION_UPDATE_WIDGET_PARAM, recipe);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                Recipe recipe = intent.getParcelableExtra(ACTION_UPDATE_WIDGET_PARAM);
                handleUpdateWidget(recipe);
            }
        }
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleUpdateWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_ingredients_list);

        //Now update all widgets
        BakingAppWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds, recipe);
    }

}

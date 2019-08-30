package com.example.baking_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.baking_app.fragments.RecipeDetailFragment;
import com.example.baking_app.models.Ingredient;
import com.example.baking_app.models.Recipe;
import com.example.baking_app.provider.IngredientContract;
import com.example.baking_app.provider.IngredientDBHelper;
import com.example.baking_app.widget.RecipeIngredientsService;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.baking_app.provider.IngredientContract.BASE_CONTENT_URI;
import static com.example.baking_app.provider.IngredientContract.PATH_INGREDIENTS;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        setSupportActionBar(toolbar);

        // Bind the views
        ButterKnife.bind(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearTable(view.getContext());

                long timeNow = System.currentTimeMillis();
                // Insert the new ingredient into DB
                for(Ingredient ingredient : mRecipe.getIngredients()){
                    addDatabase(ingredient);
                }

                RecipeIngredientsService.startActionUpdateWidget(view.getContext(), mRecipe);
                Toast.makeText(view.getContext(), "Ingredients added to widget", Toast.LENGTH_LONG).show();
            }

            public void addDatabase(Ingredient ingredient){
                long timeNow = System.currentTimeMillis();
                ContentValues contentValues = new ContentValues();
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_INGREDIENT_DESCRIPTION, ingredient.getDescription());
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_RECIPE, mRecipe.getName());
                contentValues.put(IngredientContract.IngredientEntry.COLUMN_CREATION_TIME, timeNow);
                getContentResolver().insert(IngredientContract.IngredientEntry.CONTENT_URI, contentValues);

            }


            public void clearTable(Context context) {
                Cursor mCursor;

                Uri INGREDIENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();
                mCursor = context.getContentResolver().query(INGREDIENT_URI, null, null, null,null);

                if(mCursor.getCount() > 0) {
                    for (int i = 0; i < mCursor.getCount(); i++) {
                        mCursor.moveToPosition(i);
                        int idIndex = mCursor.getColumnIndex(IngredientContract.IngredientEntry._ID);
                        int ingredientID = mCursor.getInt(idIndex);

                        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
                                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build(), ingredientID);
                        getContentResolver().delete(SINGLE_PLANT_URI, null, null);
                    }
                }
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mRecipe = getIntent().getParcelableExtra(RecipeDetailFragment.ARG_ITEM_ID);
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }else{
            mRecipe = savedInstanceState.getParcelable(RecipeDetailFragment.ARG_ITEM_ID);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && mRecipe != null) {
            appBarLayout.setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

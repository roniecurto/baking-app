package com.example.baking_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.baking_app.adapters.RecipeAdapter;
import com.example.baking_app.contants.Constants;
import com.example.baking_app.idlingresource.SimpleIdlingResource;
import com.example.baking_app.models.Recipe;
import com.example.baking_app.utils.JsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.item_list) RecyclerView mRecyclerView;

    @Nullable
    @BindView(R.id.item_detail_container) FrameLayout itemDetailContainer;

    private RecipeAdapter mRecipeAdapter;
    private List<Recipe> recipeList = new ArrayList<Recipe>();
    GridLayoutManager layoutManager;
    int spanCount = 1;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        // Bind the views
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (itemDetailContainer != null) {
            mTwoPane = true;
        }else {
            mTwoPane = false;
        }

        //recycle view setup
        layoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecipeAdapter = new RecipeAdapter(this ,recipeList, mTwoPane);
        mRecyclerView.setAdapter(mRecipeAdapter);

        requestDataVolley();

        // Get the IdlingResource instance
        mIdlingResource = getIdlingResource();
    }

    private void requestDataVolley(){
        String urlRecipesList = Constants.RECIPES_URL;
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
            Request.Method.GET,
            urlRecipesList,
            null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    recipeList = JsonUtils.parseRecipeListJson(response);
                    mRecipeAdapter.setData(recipeList);
                    mIdlingResource.setIdleState(true);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    recipeList = null;
                }
            }
        );

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

}

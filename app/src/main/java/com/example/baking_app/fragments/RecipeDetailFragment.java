package com.example.baking_app.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.baking_app.ItemDetailActivity;
import com.example.baking_app.ItemListActivity;
import com.example.baking_app.R;
import com.example.baking_app.adapters.IngredientAdapter;
import com.example.baking_app.adapters.StepAdapter;
import com.example.baking_app.idlingresource.SimpleIdlingResource;
import com.example.baking_app.models.Recipe;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    @BindView(R.id.ingredients_list) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.steps_list) RecyclerView mStepsRecyclerView;

    public static final String ARG_ITEM_ID = "recipe";
    LinearLayoutManager ingredientsLayoutManager;
    LinearLayoutManager stepsLayoutManager;
    private Recipe mItem;

    private IngredientAdapter mIngredientAdapter;
    private StepAdapter mStepsAdapter;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            if (getArguments().containsKey(ARG_ITEM_ID)) {
                mItem = getArguments().getParcelable(ARG_ITEM_ID);
            }
        }else{
            mItem = savedInstanceState.getParcelable(ARG_ITEM_ID);
        }

        // Get the IdlingResource instance
        mIdlingResource = getIdlingResource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        // Bind the views
        ButterKnife.bind(this, rootView);

        if (mItem != null) {
            //recycle view setup
            ingredientsLayoutManager = new LinearLayoutManager(rootView.getContext());
            mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
            mIngredientsRecyclerView.setHasFixedSize(true);
            mIngredientAdapter = new IngredientAdapter(mItem);
            mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

            stepsLayoutManager = new LinearLayoutManager(rootView.getContext());
            mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
            mStepsRecyclerView.setHasFixedSize(true);
            mStepsAdapter = new StepAdapter(mItem);
            mStepsRecyclerView.setAdapter(mStepsAdapter);

            mIdlingResource.setIdleState(true);
        }

        return rootView;
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_ITEM_ID, mItem);
        super.onSaveInstanceState(outState);
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

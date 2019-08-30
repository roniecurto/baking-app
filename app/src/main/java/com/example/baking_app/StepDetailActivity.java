package com.example.baking_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.baking_app.fragments.RecipeDetailFragment;
import com.example.baking_app.fragments.StepDetailFragment;
import com.example.baking_app.models.Recipe;
import com.example.baking_app.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.btn_next) Button mButtonNext;
    @Nullable
    @BindView(R.id.btn_previous) Button mButtonPrevious;

    @Nullable
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout appBarLayout;

    private Recipe mRecipe;
    private Step mStep;
    private int stepIndex;
    private List<Step> steps = new ArrayList<Step>();
    private int stepsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        // Bind the views
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mStep = getIntent().getParcelableExtra(StepDetailFragment.ARG_ITEM_ID);
            mRecipe = getIntent().getParcelableExtra(RecipeDetailFragment.ARG_ITEM_ID);

            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.ARG_ITEM_ID, mStep);
            arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.step_detail_container, fragment)
                .commit();
        }else{
            mStep = savedInstanceState.getParcelable(StepDetailFragment.ARG_ITEM_ID);
            mRecipe = savedInstanceState.getParcelable(RecipeDetailFragment.ARG_ITEM_ID);
        }


        if(mStep != null){
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNavigation(-1);
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepNavigation(1);
            }
        });

        setupButtons();
    }

    public void setupButtons(){
        steps = mRecipe.getSteps();
        stepsSize = steps.size();
        stepIndex = mRecipe.checkStep(mStep);

        //first step. hide previous button
        if(stepIndex == 0) {
            mButtonPrevious.setVisibility(View.GONE);
        }

        //not the first not the last. show all buttons
        if(stepIndex > 0 && stepIndex < stepsSize -1) {
            mButtonPrevious.setVisibility(View.VISIBLE);
            mButtonNext.setVisibility(View.VISIBLE);
        }

        //last step. hide next button
        if(stepIndex == stepsSize -1){
            mButtonNext.setVisibility(View.GONE);
        }

        //unknown step
        if(stepIndex == -1){
            mButtonPrevious.setVisibility(View.GONE);
            mButtonNext.setVisibility(View.GONE);
        }
    }

    public void stepNavigation(int jump){
        int stepIndexRequested = stepIndex + jump;


        if(stepIndexRequested < 0 || stepIndexRequested > stepsSize - 1){
            Toast.makeText(this, "Invalid step requested.", Toast.LENGTH_LONG).show();
        }else{
            Step stepRequested = steps.get(stepIndexRequested);
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, stepRequested);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
        outState.putParcelable(StepDetailFragment.ARG_ITEM_ID, mStep);
        super.onSaveInstanceState(outState);
    }
}

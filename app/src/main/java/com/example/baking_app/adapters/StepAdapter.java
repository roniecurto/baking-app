package com.example.baking_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking_app.R;
import com.example.baking_app.StepDetailActivity;
import com.example.baking_app.fragments.RecipeDetailFragment;
import com.example.baking_app.fragments.StepDetailFragment;
import com.example.baking_app.models.Recipe;
import com.example.baking_app.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private List<Step> mStepData = new ArrayList<Step>();
    private Recipe mRecipe;

    //private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step item = (Step) view.getTag();

            Context context = view.getContext();
            Intent intent = new Intent(context, StepDetailActivity.class);
            intent.putExtra(StepDetailFragment.ARG_ITEM_ID, item);
            intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, mRecipe);
            context.startActivity(intent);
        }
    };


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param recipe  Recipe
     */
    public StepAdapter(Recipe recipe) {
        mRecipe = recipe;

        if(mRecipe != null){
            mStepData = mRecipe.getSteps();
        }
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class StepAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mStepShortDescriptionTextView;

        private StepAdapterViewHolder(View view) {
            super(view);
            mStepShortDescriptionTextView = view.findViewById(R.id.step_short_description);
        }

    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieAdapterViewHolder that holds the View for each list item
     */
    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list_step, viewGroup, false);
        return new StepAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param stepAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final StepAdapterViewHolder stepAdapterViewHolder, int position) {
        Step step = (Step) mStepData.get(position);

        if(step != null) {
            stepAdapterViewHolder.mStepShortDescriptionTextView.setText(step.getShortDescription());

            stepAdapterViewHolder.itemView.setTag(step);
            stepAdapterViewHolder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if(mStepData != null) return mStepData.size();

        return 0;
    }

}


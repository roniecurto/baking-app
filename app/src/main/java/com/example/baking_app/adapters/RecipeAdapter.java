package com.example.baking_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking_app.ItemDetailActivity;
import com.example.baking_app.ItemListActivity;
import com.example.baking_app.R;
import com.example.baking_app.fragments.RecipeDetailFragment;
import com.example.baking_app.models.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipeData;
    private final ItemListActivity mParentActivity;
    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Recipe item = (Recipe) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(RecipeDetailFragment.ARG_ITEM_ID, item);
                RecipeDetailFragment fragment = new RecipeDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, item);

                context.startActivity(intent);
            }
        }
    };


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param parent    ItemListActivity
     * @param recipeItems    ArrayList of Recipe
     * @param twoPane The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public RecipeAdapter(ItemListActivity parent, List<Recipe> recipeItems, boolean twoPane) {
        mRecipeData = recipeItems;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mRecipeIdTextView;
        private final TextView mRecipeTitleTextView;

        private RecipeAdapterViewHolder(View view) {
            super(view);
            mRecipeIdTextView = view.findViewById(R.id.id_text);
            mRecipeTitleTextView = view.findViewById(R.id.content);
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
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_list_recipe, viewGroup, false);
        return new RecipeAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param recipeAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        Recipe recipeClicked = (Recipe) mRecipeData.get(position);
        recipeAdapterViewHolder.mRecipeIdTextView.setText(String.valueOf(recipeClicked.getId()));
        recipeAdapterViewHolder.mRecipeTitleTextView.setText(recipeClicked.getName());

        recipeAdapterViewHolder.itemView.setTag(recipeClicked);
        recipeAdapterViewHolder.itemView.setOnClickListener(mOnClickListener);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie list
     */
    @Override
    public int getItemCount() {
        if(mRecipeData != null) return mRecipeData.size();

        return 0;
    }

    public void setData(List recipeList){
        mRecipeData = recipeList;
        notifyDataSetChanged();
    }
}


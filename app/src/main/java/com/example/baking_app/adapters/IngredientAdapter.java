package com.example.baking_app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baking_app.R;
import com.example.baking_app.models.Ingredient;
import com.example.baking_app.models.Recipe;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private List<Ingredient> mIngredientData = new ArrayList<Ingredient>();
    private Recipe mRecipe;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param recipe Recipe
     */
    public IngredientAdapter(Recipe recipe) {
        mRecipe = recipe;

        if(mRecipe != null) {
            mIngredientData = recipe.getIngredients();
        }
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder {
        private final TextView mIngredientTextView;

        private IngredientAdapterViewHolder(View view) {
            super(view);
            mIngredientTextView = view.findViewById(R.id.ingredient_line);
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
    public IngredientAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ingredient_list, viewGroup, false);
        return new IngredientAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the Recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param ingredientAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final IngredientAdapterViewHolder ingredientAdapterViewHolder, int position) {
        Ingredient ingredient = (Ingredient) mIngredientData.get(position);

        if(ingredient != null) {
            ingredientAdapterViewHolder.mIngredientTextView.setText(ingredient.getDescription());
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
        if(mIngredientData != null) return mIngredientData.size();

        return 0;
    }

}

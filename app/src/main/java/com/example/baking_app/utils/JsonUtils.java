package com.example.baking_app.utils;

import com.example.baking_app.models.Ingredient;
import com.example.baking_app.models.Recipe;
import com.example.baking_app.models.Step;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List parseRecipeListJson(JSONArray recipes) {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        List<Step> stepsList;
        List<Ingredient> ingredientsList;

        Recipe recipeObject;

        try {
            JSONArray steps;
            JSONArray ingredients;
            JSONObject recipe;

            if (recipes.length() > 0) {
                for (int i = 0; i < recipes.length(); i++) {
                    recipe = recipes.getJSONObject(i);
                    steps = recipe.getJSONArray("steps");
                    ingredients = recipe.getJSONArray("ingredients");

                    stepsList = parseRecipeStepListJson(steps);
                    ingredientsList = parseRecipeIngredientListJson(ingredients);

                    recipeObject = new Recipe(
                            recipe.getInt("id"),
                            recipe.getString("name"),
                            recipe.getInt("servings"),
                            recipe.getString("image"),
                            stepsList,
                            ingredientsList
                    );

                    recipeList.add(recipeObject);
                }
            }

            return recipeList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List parseRecipeStepListJson(JSONArray json) {
        List<Step> stepsList = new ArrayList<Step>();
        Step stepObject;

        try {
            JSONObject step;

            if (json.length() > 0) {
                for (int i = 0; i < json.length(); i++) {
                    step = json.getJSONObject(i);

                    stepObject = new Step(
                            step.getInt("id"),
                            step.getString("shortDescription"),
                            step.getString("description"),
                            step.getString("videoURL"),
                            step.getString("thumbnailURL")
                    );

                    stepsList.add(stepObject);
                }
            }

            return stepsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List parseRecipeIngredientListJson(JSONArray json) {
        List<Ingredient> ingredientsList = new ArrayList<Ingredient>();
        Ingredient ingredientObject;

        try {
            JSONObject ingredient;

            if (json.length() > 0) {
                for (int i = 0; i < json.length(); i++) {
                    ingredient = json.getJSONObject(i);

                    ingredientObject = new Ingredient(
                            ingredient.getDouble("quantity"),
                            ingredient.getString("measure"),
                            ingredient.getString("ingredient")
                    );

                    ingredientsList.add(ingredientObject);
                }
            }

            return ingredientsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


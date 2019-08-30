package com.example.baking_app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private int servings;
    private String image;
    private List<Step> steps = new ArrayList<Step>();
    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

    public Recipe(int id, String name, int servings, String image, List<Step> steps, List<Ingredient> ingredients){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    private Recipe(Parcel in){
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        in.readList(steps, Step.class.getClassLoader());
        in.readList(ingredients, Ingredient.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeList(steps);
        parcel.writeList(ingredients);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };

    //Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return image;
    }
    public int getServings() {
        return servings;
    }
    public List<Step> getSteps() {
        return steps;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    //Setters
    public void setId(int id){ this.id = id; }
    public void setName(String name){ this.name = name; }
    public void setServings(int servings){ this.servings = servings; }
    public void setImage(String image){ this.image = image; }
    public void setSteps(List<Step> steps){ this.steps = steps; }
    public void setIngredients(List<Ingredient> ingredients){ this.ingredients = ingredients; }

    //checking step existence
    public int checkStep(Step stepWanted){
        int index = 0;
        boolean foundStep = false;

        for(Step step : steps){
            if(step.getId() == stepWanted.getId()){
                foundStep = true;
                break;
            }
            index++;
        }

        if(foundStep) return index;

        return -1;
    }
}

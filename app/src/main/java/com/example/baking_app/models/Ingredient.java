package com.example.baking_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(double quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    private Ingredient(Parcel in){
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }
    };

    //Getters
    public double getQuantity() {
        return quantity;
    }
    public String getIngredient() {
        return ingredient;
    }
    public String getMeasure() {
        return measure;
    }
    public String getDescription(){ return "- " + quantity + " " + measure + " of " + ingredient + "."; }

    //Setters
    public void setQuantity(Double quantity){ this.quantity = quantity; }
    public void setMeasure(String measure){ this.measure = measure; }
    public void setIngredient(String ingredient){ this.ingredient = ingredient; }
}

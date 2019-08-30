package com.example.baking_app.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class IngredientDBHelper extends SQLiteOpenHelper {
    // The database name
    private static final String DATABASE_NAME = "bakingapp.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public IngredientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the plants data
        final String SQL_CREATE_PLANTS_TABLE = "CREATE TABLE " + IngredientContract.IngredientEntry.TABLE_NAME + " (" +
                IngredientContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                IngredientContract.IngredientEntry.COLUMN_INGREDIENT_DESCRIPTION + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_RECIPE + " TEXT NOT NULL, " +
                IngredientContract.IngredientEntry.COLUMN_CREATION_TIME + " TIMESTAMP NOT NULL)";

        sqLiteDatabase.execSQL(SQL_CREATE_PLANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + IngredientContract.IngredientEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

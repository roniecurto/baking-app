package com.example.baking_app.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.baking_app.provider.IngredientContract.IngredientEntry.TABLE_NAME;

public class IngredientContentProvider extends ContentProvider {
    // Define final integer constants for the directory of ingredients and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int INGREDIENTS = 100;
    public static final int INGREDIENT_WITH_ID = 101;

    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = IngredientContentProvider.class.getName();

    // Define a static buildUriMatcher method that associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(IngredientContract.AUTHORITY, IngredientContract.PATH_INGREDIENTS + "/#", INGREDIENT_WITH_ID);
        return uriMatcher;
    }

    // Member variable for a IngredientDBHelper that's initialized in the onCreate() method
    private IngredientDBHelper mIngredientDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mIngredientDBHelper = new IngredientDBHelper(context);
        return true;
    }

    /***
     * Handles requests to insert a single new row of data
     *
     * @param uri
     * @param values
     * @return
     */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mIngredientDBHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the ingredients directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        switch (match) {
            case INGREDIENTS:
                // Insert new values into the database
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(IngredientContract.IngredientEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    /***
     * Handles requests for data by URI
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mIngredientDBHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the ingredients directory
            case INGREDIENTS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    /***
     * Deletes a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mIngredientDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted items
        int ingredientsDeleted; // starts as 0
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case INGREDIENT_WITH_ID:
                // Get the ingredient ID from the URI path
                //String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                ingredientsDeleted = db.delete(TABLE_NAME, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Notify the resolver of a change and return the number of items deleted
        if (ingredientsDeleted != 0) {
            // An ingredient (or more) was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of ingredient deleted
        return ingredientsDeleted;
    }



    /***
     * Updates a single row of data
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return number of rows affected
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // Get access to underlying database
        final SQLiteDatabase db = mIngredientDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated records
        int ingredientsUpdated;

        switch (match) {
            case INGREDIENTS:
                ingredientsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case INGREDIENT_WITH_ID:
                if (selection == null) selection = IngredientContract.IngredientEntry._ID + "=?";
                else selection += " AND " + IngredientContract.IngredientEntry._ID + "=?";
                // Get the place ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Append any existing selection options to the ID filter
                if (selectionArgs == null) selectionArgs = new String[]{id};
                else {
                    ArrayList<String> selectionArgsList = new ArrayList<String>();
                    selectionArgsList.addAll(Arrays.asList(selectionArgs));
                    selectionArgsList.add(id);
                    selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);
                }
                ingredientsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items updated
        if (ingredientsUpdated != 0) {
            // A place (or more) was updated, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of places deleted
        return ingredientsUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

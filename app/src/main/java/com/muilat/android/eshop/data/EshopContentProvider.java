package com.muilat.android.eshop.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import static com.muilat.android.eshop.data.EshopContract.ItemEntry;


public class EshopContentProvider  extends ContentProvider {

    public static final int CARTS = 100;
    public static final int ITEMS = 110;
    public static final int CART_WITH_ID = 101;
    public static final int ITEM_WITH_ID = 102;


    // Declare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = EshopContentProvider.class.getSimpleName();

    // Define a static buildUriMatcher method that associates URI's with their int match
    /**
     Initialize a new matcher object without any matches,
     then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    /*
      All paths added to the UriMatcher have a corresponding int.
      For each kind of uri you may want to access, add the corresponding match with addURI.
      The two calls below add matches for the task directory and a single item by ID.
     */
        uriMatcher.addURI(EshopContract.AUTHORITY, EshopContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(EshopContract.AUTHORITY, EshopContract.PATH_ITEMS + "/#", ITEM_WITH_ID);
        uriMatcher.addURI(EshopContract.AUTHORITY, CartContract.PATH_CART, CARTS);
        uriMatcher.addURI(EshopContract.AUTHORITY, CartContract.PATH_CART + "/#", CART_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a EshopDbHelper that's initialized in the onCreate() method
    private EshopDbHelper mEshopDbHelper;

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a WazoDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable

        Context context = getContext();
        mEshopDbHelper = new EshopDbHelper(context);
        return true;
    }


    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = mEshopDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the items directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned
        long id;
        switch (match) {
            case ITEMS:
                // Insert new values into the database
                // Inserting values into items table
                id = db.insert(ItemEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(EshopContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case CARTS:
                // Insert new values into the database
                // Inserting values into carts table
                id = db.insert(CartContract.CartEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(CartContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mEshopDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        String id;

        // Query for the items directory and a default case
        switch (match) {
            // Query for the items directory
            case ITEMS:
                retCursor =  db.query(ItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single item case, recognized by the ID included in the URI path
            case ITEM_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(ItemEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            // Query for the carts directory
            case CARTS:
                retCursor =  db.query(CartContract.CartEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Handle the single cart case, recognized by the ID included in the URI path
            case CART_WITH_ID:
                // Get the task ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                retCursor =  db.query(CartContract.CartEntry.TABLE_NAME,
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


    // Implement delete to delete a single row of data
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mEshopDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted items
        int itemsDeleted; // starts as 0
        String id;
        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case ITEM_WITH_ID:
                // Get the item ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                itemsDeleted = db.delete(ItemEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            case CART_WITH_ID:
                // Get the item ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                itemsDeleted = db.delete(CartContract.CartEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (itemsDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of items deleted
        return itemsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mEshopDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of updated item
        int itemsUpdated; // starts as 0
        String id;
        // [Hint] Use selections to update an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case ITEM_WITH_ID:
                // Get the item ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemsUpdated = db.update(ItemEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            // Handle the single item case, recognized by the ID included in the URI path
            case CART_WITH_ID:
                // Get the item ID from the URI path
                id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID

                itemsUpdated = db.update(CartContract.CartEntry.TABLE_NAME,values, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (itemsUpdated != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return itemsUpdated;
    }


    @Override
    public String getType(@NonNull Uri uri) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

}


package com.muilat.android.eshop.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.muilat.android.eshop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.muilat.android.eshop.data.EshopContract.ItemEntry;

public class EshopDbHelper  extends SQLiteOpenHelper {

    private static final String TAG = EshopDbHelper.class.getName();
    private static final String DB_NAME = "eshop.db";
    private static final int DB_VERSION = 1;

    private Resources mResources;

    public EshopDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(ItemEntry.CREATE_ITEMS_SQL);/*create items table*/
        db.execSQL(CartContract.CartEntry.CREATE_CART_SQL);/*create cart table*/

        //only isert the asset from items.json if the db is being created for the first time
        addInitItems(db);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Query for altering table here
        final String ALTER_TABLE = "";

        if(ALTER_TABLE.equals("")){
            db.execSQL(ALTER_TABLE);
        }
        else
        {
            //no alter query so drop the existing database
            //drop words table
            db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CartContract.CartEntry.TABLE_NAME);



            //create a new database
            onCreate(db);

        }

    }

    public static String getColumnString(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    public static int getColumnInt(Cursor cursor, String name) {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

    public static long getColumnLong(Cursor cursor, String name) {
        return cursor.getLong(cursor.getColumnIndex(name));
    }


    /**
     * save init categories into database
     */
    private void addInitItems(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            try {
                readItemsFromResources(db);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Unable to pre-fill database with items", e);
        }
    }


    /**
     * load init words from {@link raw/wazodata.json}
     */
    private void readItemsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.items);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        //Parse resource into key/values
        JSONObject root = new JSONObject(builder.toString());


        JSONArray arr = root.getJSONArray("items");


        for (int i = 0; i<arr.length(); i++){
//        for (int i = 0; i<jsonObject.length(); i++){
            JSONObject obj = arr.getJSONObject(i);
            ContentValues contentValues = new ContentValues();
            // Put the insect  into the ContentValues
            contentValues.put(ItemEntry.COLUMN_NAME, obj.getString("name"));
            contentValues.put(ItemEntry.COLUMN_DESCRIPTION, obj.getString("description"));
            contentValues.put(ItemEntry.COLUMN_RATING, obj.getString("rating"));
            contentValues.put(ItemEntry.COLUMN_PRICE, obj.getString("price"));
            contentValues.put(ItemEntry.COLUMN_IMAGE_URL, obj.getString("imageUrl"));

            long id =db.insert(ItemEntry.TABLE_NAME,null,contentValues);

        }
    }



}

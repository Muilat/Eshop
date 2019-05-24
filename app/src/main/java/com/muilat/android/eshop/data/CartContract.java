package com.muilat.android.eshop.data;


import android.net.Uri;
import android.provider.BaseColumns;
import static android.provider.BaseColumns._ID;

public class CartContract{
    public static final String AUTHORITY = "com.muilat.android.eshop";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_CART = "cart";
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_CART).build();

    public static final class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart";

        public static final String COLUMN_ITEM_ID = "item_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE_URL = "imageUrl";


        final static String CREATE_CART_SQL = "CREATE TABLE "  + CartEntry.TABLE_NAME + " (" +
                _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                COLUMN_ITEM_ID+ " INTEGER NOT NULL, " +
                COLUMN_QUANTITY + " INTEGER NOT NULL, "+
                COLUMN_NAME +" TEXT NOT NULL, "+
                COLUMN_IMAGE_URL +" TEXT NOT NULL, "+
                COLUMN_PRICE +" REAL NOT NULL "+
                " );";

    }
}

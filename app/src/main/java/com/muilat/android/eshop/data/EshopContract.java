package com.muilat.android.eshop.data;


import android.net.Uri;
import android.provider.BaseColumns;
import static android.provider.BaseColumns._ID;

public class EshopContract{
    public static final String AUTHORITY = "com.muilat.android.eshop";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_ITEMS = "items";
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

    public static final class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_DESCRIPTION = "description";


    final static String CREATE_ITEMS_SQL = "CREATE TABLE "  + TABLE_NAME + " (" +
            _ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_NAME+ " TEXT NOT NULL, " +
            COLUMN_PRICE + " REAL NOT NULL,"+
            COLUMN_DESCRIPTION + " TEXT NOT NULL,"+
            COLUMN_IMAGE_URL + " TEXT NOT NULL,"+
            COLUMN_RATING + " INTNOT NULL DEFAULT 0);";

    }
}

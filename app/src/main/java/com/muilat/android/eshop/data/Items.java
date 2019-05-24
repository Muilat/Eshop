package com.muilat.android.eshop.data;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.muilat.android.eshop.data.EshopContract.ItemEntry;

public class Items implements Parcelable {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    private int rating;

    public Items(String name, String description, String imageUrl, double price, int rating) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.rating = rating;
    }

    public Items(Cursor cursor){
        id = EshopDbHelper.getColumnInt(cursor, ItemEntry._ID);
        name = EshopDbHelper.getColumnString(cursor, ItemEntry.COLUMN_NAME);
        description = EshopDbHelper.getColumnString(cursor, ItemEntry.COLUMN_DESCRIPTION);
        imageUrl = EshopDbHelper.getColumnString(cursor, ItemEntry.COLUMN_IMAGE_URL);
        price = EshopDbHelper.getColumnLong(cursor, ItemEntry.COLUMN_PRICE);
        rating = EshopDbHelper.getColumnInt(cursor, ItemEntry.COLUMN_RATING);
    }

    protected Items(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        price = in.readDouble();
        rating = in.readInt();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(imageUrl);
        parcel.writeDouble(price);
        parcel.writeInt(rating);
    }
}

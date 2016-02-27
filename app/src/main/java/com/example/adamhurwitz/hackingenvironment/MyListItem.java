package com.example.adamhurwitz.hackingenvironment;

import android.database.Cursor;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;

public class MyListItem {
    static String imageUrl;
    static String title;
    static String price;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setPrice(String price) {
        this.price = "$"+price;
    }

    public String getPrice() {
        return price;
    }

    public static MyListItem fromCursor(Cursor cursor) {
        String imageUrl = cursor.getString(cursor.getColumnIndex(
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_IMAGEURL));
        String title = cursor.getString(cursor.getColumnIndex(
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_TITLE));
        String price = cursor.getString(cursor.getColumnIndex(
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_PRICE));
        MyListItem myListItem = new MyListItem();
        myListItem.setImageUrl(imageUrl);
        myListItem.setTitle(title);
        myListItem.setPrice(price);
        return myListItem;
    }
}
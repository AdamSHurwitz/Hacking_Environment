package com.example.adamhurwitz.hackingenvironment.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract
        .ContentProviderProductData;

/**
 * Manages a local database.
 */
public class ContentProviderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "contentprovider_doodle_products.db";

    public ContentProviderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PRODUCTTABLE = "CREATE TABLE " + ContentProviderContract
                .ContentProviderProductData.TABLE_NAME + "(" +
                // AutoIncrement
                ContentProviderProductData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContentProviderProductData.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                ContentProviderProductData.COLUMN_NAME_IMAGEURL + " TEXT NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_ITEMID + " TEXT UNIQUE NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_POPULARITY + " REAL NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_PRICE + " INTEGER NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_RECENT + " INTEGER NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_RELEASEDATE + " TEXT NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_SEARCHSTRINGS + " TEXT NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_VINTAGE + " INTEGER NOT NULL, " +
                ContentProviderProductData.COLUMN_NAME_FAVORITE + " INTEGER, " +
                ContentProviderProductData.COLUMN_NAME_CART + " INTEGER " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContentProviderProductData.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

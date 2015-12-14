package com.example.adamhurwitz.hackingenvironment.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adamhurwitz.hackingenvironment.data.CursorContract.ProductData;

/**
 * Manages a local database.
 */
public class CursorDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "doodle_products.db";

    public CursorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PRODUCTTABLE = "CREATE TABLE " + ProductData.TABLE_NAME + "(" +
                // AutoIncrement
                ProductData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductData.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                ProductData.COLUMN_NAME_IMAGEURL + " TEXT NOT NULL, " +
                ProductData.COLUMN_NAME_ITEMID + " TEXT UNIQUE NOT NULL, " +
                ProductData.COLUMN_NAME_POPULARITY + " REAL NOT NULL, " +
                ProductData.COLUMN_NAME_PRICE + " INTEGER NOT NULL, " +
                ProductData.COLUMN_NAME_RECENT + " INTEGER NOT NULL, " +
                ProductData.COLUMN_NAME_RELEASEDATE + " TEXT NOT NULL, " +
                ProductData.COLUMN_NAME_SEARCHSTRINGS + " TEXT NOT NULL, " +
                ProductData.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                ProductData.COLUMN_NAME_VINTAGE + " INTEGER NOT NULL, " +
                ProductData.COLUMN_NAME_FAVORITE + " INTEGER, " +
                ProductData.COLUMN_NAME_CART + " INTEGER " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PRODUCTTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProductData.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

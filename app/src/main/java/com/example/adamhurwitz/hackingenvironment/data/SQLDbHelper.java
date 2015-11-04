package com.example.adamhurwitz.hackingenvironment.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.adamhurwitz.hackingenvironment.data.Contract.Tests;
import com.example.adamhurwitz.hackingenvironment.data.Contract.Concepts;

/**
 * Manages a local database.
 */
public class SQLDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "database.db";

    public SQLDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TESTSTABLE = "CREATE TABLE " + Tests.TABLE_NAME + "(" +
                // AutoIncrement
                Tests._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Tests.COLUMN_NAME_CONCEPT + " TEXT NOT NULL, " +
                Tests.COLUMN_NAME_TAB_NUMBER + " INTEGER NOT NULL" +
                " );";

        final String SQL_CREATE_CONCEPTSTABLE = "CREATE TABLE " + Concepts.TABLE_NAME + "(" +
                // AutoIncrement
                Concepts._ID + " INTEGER PRIMARY KEY," +
                Concepts.COLUMN_NAME_CONCEPT + " TEXT UNIQUE NOT NULL, " +
                Concepts.COLUMN_NAME_AND_COURSE + " TEXT NOT NULL, " +
                Concepts.COLUMN_NAME_DIFF_LVL + " INTEGER NOT NULL, " +

                // Set up the COLUMN_NAME_ENTRY_ID as a foreign key to primary table.
                " FOREIGN KEY (" + Concepts.COLUMN_NAME_CONCEPT + ") REFERENCES " +
                Tests.TABLE_NAME + " (" + Tests.COLUMN_NAME_CONCEPT + ") " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_TESTSTABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONCEPTSTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tests.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Concepts.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.adamhurwitz.hackingenvironment.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class ProductProvider extends ContentProvider {
    private static final String LOG_TAG = ProductProvider.class.getSimpleName();

    // The URI Matcher used by this content provider.
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private ContentProviderDbHelper cursorDbHelper;
    // Defines the database name
    private static final String DBNAME = "doodle_products.db";
    static final int PRODUCT = 85;
    Context context;


    @Override
    public boolean onCreate() {
        cursorDbHelper = new ContentProviderDbHelper(getContext());
        return true;
    }

    /*
       Students: Here is where you need to create the UriMatcher. This UriMatcher will
       match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
       and LOCATION integer constants defined above.  You can test this by uncommenting the
       testUriMatcher test within TestUriMatcher.
    */
    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ContentProviderContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding URI.
        matcher.addURI(authority, ContentProviderContract.PATH_PRODUCTTABLE, PRODUCT);
        // use 'ContentProviderContract.PATH_PRODUCT + "/*"' for path with one additional item
        // string or 'ContentProviderContract.PATH_PRODUCT + "/*/#"' for path with string then a num
        //TODO: should return 100?
        Log.v(LOG_TAG, "UriMatcher_returns :" + String.valueOf(matcher));
        return matcher;
    }


    // getType() not important since we're not working with BLOB data type
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = uriMatcher.match(uri);

        switch (match) {
            case PRODUCT:
                //TODO: should return something
                Log.v(LOG_TAG, "getType :" + ContentProviderContract.ContentProviderProductData
                        .CONTENT_TYPE);
                return ContentProviderContract.ContentProviderProductData.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case PRODUCT: {
                //retCursor = productCursor(uri, projection, sortOrder);
                cursor = cursorDbHelper.getReadableDatabase().query(
                        ContentProviderContract.ContentProviderProductData.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = cursorDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PRODUCT: {
                long _id = db.insert(ContentProviderContract.ContentProviderProductData.TABLE_NAME,
                        null, values);
                if (_id > 0)
                    returnUri = ContentProviderContract.ContentProviderProductData.buildProductUri(
                            _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = cursorDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case PRODUCT:
                rowsUpdated = db.update(ContentProviderContract.ContentProviderProductData
                                .TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = cursorDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case PRODUCT:
                rowsDeleted = db.delete(
                        ContentProviderContract.ContentProviderProductData.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = cursorDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(ContentProviderContract.ContentProviderProductData
                                        .TABLE_NAME, null,
                                value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        cursorDbHelper.close();
        super.shutdown();
    }

}
package com.example.adamhurwitz.hackingenvironment.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class ContentProviderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ContentProviderContract(){}

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.adamhurwitz.hackingenvironment";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_PRODUCT = "product_table";

    /*
        Inner class that defines the table contents of the table
     */
    public static abstract class ProductData implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PRODUCT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_PRODUCT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/"
                        + PATH_PRODUCT;

        public static final String TABLE_NAME = "product_table";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGEURL = "image_url";
        public static final String COLUMN_NAME_ITEMID = "item_id";
        public static final String COLUMN_NAME_POPULARITY  = "popularity";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_RECENT = "recent";
        public static final String COLUMN_NAME_RELEASEDATE = "release_date";
        public static final String COLUMN_NAME_SEARCHSTRINGS = "search_strings";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VINTAGE = "vintage";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
        public static final String COLUMN_NAME_CART = "cart";

        public static Uri buildProductUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
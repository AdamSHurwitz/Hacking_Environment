package com.example.adamhurwitz.hackingenvironment.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class CursorContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public CursorContract(){}

    /*
        Inner class that defines the table contents of the table
     */
    public static abstract class ProductData implements BaseColumns {
        public static final String  TABLE_NAME = "product_table";
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
    }

}
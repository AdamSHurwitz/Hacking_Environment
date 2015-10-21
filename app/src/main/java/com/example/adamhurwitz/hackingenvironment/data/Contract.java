package com.example.adamhurwitz.hackingenvironment.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 */
public class Contract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public Contract (){}

    /*
        Inner class that defines the table contents of the table
     */
    public static abstract class Tests implements BaseColumns {
        public static final String TABLE_NAME = "tests";
        public static final String COLUMN_NAME_CONCEPT = "concept";
        public static final String COLUMN_NAME_TAB_NUMBER = "tab_number";
    }

    public static final class Concepts implements BaseColumns{
        public static final String TABLE_NAME = "concepts";
        public static final String COLUMN_NAME_CONCEPT = "concept";
        public static final String COLUMN_NAME_AND_COURSE = "and_course";
        public static final String COLUMN_NAME_DIFF_LVL = "diff_lvl";
    }
}
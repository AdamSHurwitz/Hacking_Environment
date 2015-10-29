package com.example.adamhurwitz.hackingenvironment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.adamhurwitz.hackingenvironment.data.Contract;
import com.example.adamhurwitz.hackingenvironment.data.SQLDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class SQLiteTestFragment extends Fragment {
    private final String LOG_TAG = SQLiteTestFragment.class.getSimpleName();

    public SQLiteTestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.sqlite_test, container, false);

        Button getFromSharedPref = (Button) inflatedView.findViewById(R.id.putInfoIntoDatabase);
        getFromSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInfoIntoDatabase(v);
            }
        });

        Button getInfoFromDatabase = (Button) inflatedView.findViewById(R.id.getInfoFromDatabase);
        getInfoFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoFromDatabase(v);
            }
        });

        return inflatedView;
    }

    // Database Test

    // Enter info into database
    public void putInfoIntoDatabase(View view) {
        Log.v("putInfoIntoDatabase", "called here");

        // Access database
        SQLDbHelper mDbHelper = new SQLDbHelper(getContext());

        // Put Info into Database

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Contract.Tests.COLUMN_NAME_CONCEPT, "new activity");
        values.put(Contract.Tests.COLUMN_NAME_TAB_NUMBER, 1);

        // Insert the new row, returning the primary key value of the new row
        /*long thisRowID;
        thisRowID = db.insert(
                Contract.Tests.TABLE_NAME,
                null,
                values);*/
    }

    // Pull info from database
    public void getInfoFromDatabase(View view) {
        Log.v("getInfoFromDatabase", "called here");

        // Access database
        SQLDbHelper mDbHelper = new SQLDbHelper(getContext());

        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Contract.Tests._ID,
                Contract.Tests.COLUMN_NAME_CONCEPT,
                Contract.Tests.COLUMN_NAME_TAB_NUMBER
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.Tests._ID + " DESC";

        Cursor c = db.query(
                Contract.Tests.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        // Get Entries From Query

        // Get itemId entry
        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(Contract.Tests._ID));
        Log.v("getInfoFromdatabase", "OMEGA: " + String.valueOf(itemId));

        /*c.moveToNext();
        long itemId2 = c.getLong(
                c.getColumnIndexOrThrow(Contract.Tests._ID));
        Log.v("getInfoFromdatabase", "OMEGA: " + String.valueOf(itemId2));*/

        // Get columnNameConcept entry
        c.moveToFirst();
        String columnNameConcept = c.getString(
                c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_CONCEPT));
        Log.v("getInfoFromdatabase", "OMEGA2: " + columnNameConcept);

       /* c.moveToNext();
        String columnNameConcept2 = c.getString(
                c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_CONCEPT));
        Log.v("getInfoFromdatabase", "OMEGA2: " + columnNameConcept2);*/

        // Get columnTabNum entry
        c.moveToFirst();
        long columnTabNum = c.getLong(
                c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_TAB_NUMBER));
        Log.v("getInfoFromdatabase", "OMEGA3: " + String.valueOf(columnTabNum));

       /* c.moveToNext();
        long columnTabNum2 = c.getLong(
                c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_TAB_NUMBER));
        Log.v("getInfoFromdatabase", "OMEGA3: " + String.valueOf(columnTabNum2));*/


        // Place Entries Into TextViews

        // Place itemId into TextView
        TextView SQLiteIdOutput = (TextView) getActivity().findViewById(R.id.SQLite_ID_Output);
        SQLiteIdOutput.setText(String.valueOf(itemId));

        /*TextView SQLiteIdOutput2 = (TextView) getActivity().findViewById(R.id.SQLite_ID_Output2);
        SQLiteIdOutput2.setText(String.valueOf(itemId2));*/

        // Place columnNameConcept into TextView
        TextView SQLiteConceptOutput = (TextView) getActivity()
                .findViewById(R.id.SQLite_Concept_Output);
        SQLiteConceptOutput.setText(columnNameConcept);

        /*TextView SQLiteConceptOutput2 = (TextView) getActivity()
                .findViewById(R.id.SQLite_Concept_Output2);
        SQLiteConceptOutput2.setText(columnNameConcept2);*/

        // Place columnTabNum into TextView
        TextView SQLiteTabNumOutput = (TextView) getActivity()
                .findViewById(R.id.SQLite_TabNum_Output);
        SQLiteTabNumOutput.setText(String.valueOf(columnTabNum));

        /*TextView SQLiteTabNumOutput2 = (TextView) getActivity()
                .findViewById(R.id.SQLite_TabNum_Output2);
        SQLiteTabNumOutput2.setText(String.valueOf(columnTabNum2));*/
    }
}

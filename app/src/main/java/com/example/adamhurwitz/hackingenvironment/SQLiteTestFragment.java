package com.example.adamhurwitz.hackingenvironment;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adamhurwitz.hackingenvironment.data.Contract;
import com.example.adamhurwitz.hackingenvironment.data.SQLDbHelper;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class SQLiteTestFragment extends Fragment {
    private final String LOG_TAG = SQLiteTestFragment.class.getSimpleName();

    // Custom Adapter-------------------------------------------------------------------------------

    // Create ArrayList of RowObjects and Initiate ArrayLIst
    private ArrayList<RowObject> rowObjects = new ArrayList<>();
    // Create ListView for Adapter
    private ListView sqlResultList;
    // Create Adapter
    private SQLiteArrayAdapter adapter;
    //----------------------------------------------------------------------------------------------

    public SQLiteTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View inflatedView = inflater.inflate(R.layout.sqlite_test, container, false);

        Button getFromSharedPref = (Button) inflatedView.findViewById(R.id.putInfoIntoDatabase);
        getFromSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInfoIntoDatabase();
            }
        });

        Button getInfoFromDatabase = (Button) inflatedView.findViewById(R.id.getInfoFromDatabase);
        getInfoFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfoFromDatabase();
            }
        });

        Button deleteInfoFromDatabase = (Button) inflatedView.findViewById(R.id.deleteInfoFromDatabase);
        deleteInfoFromDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfoFromDatabase();
            }
        });

        Button updateInfoButton = (Button) inflatedView.findViewById(R.id.updateInfoFromDatabase);
        updateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfoButton();
            }
        });


        // Custom Adapter---------------------------------------------------------------------------

        // Link ListView to ListView in sql_text.xml
        sqlResultList = (ListView) inflatedView.findViewById(R.id.sql_query_results);

        // Build the Adapter

        // Set the adapter for the list view
        adapter = new SQLiteArrayAdapter(getActivity(),
                // link to layout table_row_view.xml and pass in ArrayList<> rowObjects
                R.layout.table_row_view, rowObjects);
        sqlResultList.setAdapter(adapter);
        //------------------------------------------------------------------------------------------


        return inflatedView;
    }

    // Database Test

    // Enter info into database
    public void putInfoIntoDatabase() {
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
        long thisRowID;
        thisRowID = db.insert(
                Contract.Tests.TABLE_NAME,
                null,
                values);
    }

    // Pull info from database
    public void getInfoFromDatabase() {
        Log.v("getInfoFromDatabase", "called here");

        // Custom Adapter --------------------------------------------------------------------------

        // Clear Adapter
        adapter.clear();
        // Clear ArrayList<> rowObjects
        rowObjects.clear();
        //------------------------------------------------------------------------------------------

        // Access database
        SQLDbHelper mDbHelper = new SQLDbHelper(getContext());

        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Use projection to define specific columns to return
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        /*String[] projection = {
                Contract.Tests._ID,
                Contract.Tests.COLUMN_NAME_CONCEPT,
                Contract.Tests.COLUMN_NAME_TAB_NUMBER
        };*/

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.Tests._ID + " DESC";

        // If you are querying entire table, can leave everything as Null
        Cursor c = db.query(
                Contract.Tests.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Querying specific columns
        /*Cursor c = db.query(
                Contract.Tests.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );*/


        // Get Number of Entries From Query
        int count = c.getCount();
        // Place itemId into TextView
        TextView CursorCount = (TextView) getActivity().findViewById(R.id.database_size);
        CursorCount.setText(String.valueOf(count));


        // WhileLoop While There Is A Cursor
        while (c.moveToNext()) {

            // Get itemId entry
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(Contract.Tests._ID));
            Log.v("getInfoFromdatabase", "OMEGA: " + String.valueOf(itemId));

            // Get columnNameConcept entry
            String columnNameConcept = c.getString(
                    c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_CONCEPT));
            Log.v("getInfoFromdatabase", "OMEGA2: " + columnNameConcept);

            // Get columnTabNum entry
            long columnTabNum = c.getLong(
                    c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_TAB_NUMBER));
            Log.v("getInfoFromdatabase", "OMEGA3: " + String.valueOf(columnTabNum));


            // Custom Adapter ----------------------------------------------------------------------

            // Place Values Into RowObject
            RowObject entryID = new RowObject()
                    .insertID(itemId)
                    .insertConceptName(columnNameConcept)
                    .insertTabNum(String.valueOf(columnTabNum));
            rowObjects.add(entryID);
            //--------------------------------------------------------------------------------------

        }

        // Custom Adapter --------------------------------------------------------------------------

        // Update adapter
        adapter.notifyDataSetChanged();
        //------------------------------------------------------------------------------------------

        c.close();
    }

    // Delete Info From Database
    public void deleteInfoFromDatabase() {

        // Access database
        SQLDbHelper mDbHelper = new SQLDbHelper(getContext());

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.Tests._ID + " DESC";

        // If you are querying entire table, can leave everything as Null
        Cursor c = db.query(
                Contract.Tests.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Get last entry
        if (c.moveToLast()) {
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(Contract.Tests._ID));
            // Define 'where' part of query.
            String selection = Contract.Tests._ID + " LIKE ?";
            // Specify arguments in placeholder order.
            Log.v("deleteInfoIntoDatabase", String.valueOf(itemId));
            String[] selectionArgs = {String.valueOf(itemId)};
            // Issue SQL statement.
            db.delete(Contract.Tests.TABLE_NAME, selection, selectionArgs);
        }

        c.close();

    }

    public void updateInfoButton() {
        // Access database
        SQLDbHelper mDbHelper = new SQLDbHelper(getContext());

        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Contract.Tests._ID + " DESC";

        // If you are querying entire table, can leave everything as Null
        Cursor c = db.query(
                Contract.Tests.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // Get first entry
        if (c.moveToFirst()) {
            String collumnNameConcept = c.getString(
                    c.getColumnIndexOrThrow(Contract.Tests.COLUMN_NAME_CONCEPT));
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(Contract.Tests.COLUMN_NAME_CONCEPT, "updated activity");

            // Which row to update, based on the ID
            String selection = Contract.Tests.COLUMN_NAME_CONCEPT + " LIKE ?";
            String[] selectionArgs = {String.valueOf(collumnNameConcept)};
            Log.v("updateInfoFromDatabase", String.valueOf(collumnNameConcept));

            int count = db.update(
                    Contract.Tests.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }

        c.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_toast:
                Toast toast = Toast.makeText(getActivity(), "MENU BUTTON!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}

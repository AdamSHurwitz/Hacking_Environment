package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.adamhurwitz.hackingenvironment.data.Contract;
import com.example.adamhurwitz.hackingenvironment.data.SQLDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_main, container, false);

        // SharedPref test
        Button writeToSharedPref = (Button) inflatedView.findViewById(R.id.writeToSharedPref);
        writeToSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSharedPreferences(v);
            }
        });

        Button getFromSharedPref = (Button) inflatedView.findViewById(R.id.getFromSharedPref);
        getFromSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromSharedPreferences(v);
            }
        });

        // Database test
        Button putInfoIntoDatabase = (Button) inflatedView.findViewById(R.id.putInfoIntoDatabase);
        putInfoIntoDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInfoIntoDatabase(v);
            }
        });

        return inflatedView;
    }

    // Write to Shared Preferences
    public void sendToSharedPreferences(View view) {
        EditText userPref = (EditText) getActivity().findViewById(R.id.edit_message_one);
        String firstPref = userPref.getText().toString();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.preference_key), firstPref);
        editor.commit();
    }

    // Read from Shared Preferences
    public void getFromSharedPreferences(View view) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.saved_default_value);
        String preference = sharedPref.getString(getString(R.string.preference_key), defaultValue);

        TextView sharedPrefOutput = (TextView) getActivity().findViewById(R.id.shared_pref_output);
        sharedPrefOutput.setText(preference);
    }
//------------------------------------------------------------------------------------------------//
// Database Test

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
        long newRowId;
        newRowId = db.insert(
                Contract.Tests.TABLE_NAME,
                null,
                values);
    }
}

package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SharedPrefTestFragment extends Fragment {
    private final String LOG_TAG = SharedPrefTestFragment.class.getSimpleName();

    public SharedPrefTestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.shared_pref_test, container, false);

        setHasOptionsMenu(true);

        // SharedPref Test

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


        Button getFromSetting = (Button) inflatedView.findViewById(R.id.getFromSettings);
        getFromSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromSetting(v);
            }
        });

        return inflatedView;
    }

    //----------------------------------------------------------------------------------------------
    // Shared Preferences Test

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

    // Read Shared Preferences From Settings
    public void getFromSetting(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String result = prefs.getString("sharedpref_settings_key",
                "Insert text to be shown in SHAREDPREF view");
        TextView sharedPrefOutput = (TextView) getActivity().findViewById(R.id.shared_pref_output);
        sharedPrefOutput.setText(result);
    }
    //----------------------------------------------------------------------------------------------


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

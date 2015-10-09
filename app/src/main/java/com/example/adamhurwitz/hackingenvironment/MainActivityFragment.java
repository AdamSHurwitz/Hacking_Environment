package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_main, container, false);

        Button b1 = (Button) inflatedView.findViewById(R.id.writeToSharedPref);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToSharedPreferences(v);
            }
        });

        Button b2 = (Button) inflatedView.findViewById(R.id.getFromSharePref);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromSharedPreferences(v);
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
        editor.putString(getString(R.string.preference_test), firstPref);
        editor.commit();
    }

    // Read from Shared Preferences
    public void getFromSharedPreferences(View view) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.saved_default_value);
        String preference = sharedPref.getString(getString(R.string.preference_test), defaultValue);

        TextView sharedPrefOutput = (TextView) getActivity().findViewById(R.id.shared_pref_output);
        sharedPrefOutput.setText(preference);
    }
}

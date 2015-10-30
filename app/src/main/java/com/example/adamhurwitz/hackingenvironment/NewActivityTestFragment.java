package com.example.adamhurwitz.hackingenvironment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewActivityTestFragment extends Fragment {
    private final String LOG_TAG = NewActivityTestFragment.class.getSimpleName();

    public static final String EXTRA_MESSAGE = "com.example.adamhurwitz.hackingenvironment.MainActivity";

    public NewActivityTestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.new_activity_test, container, false);

        /**
         * Called when the user clicks the Send button
         */
        Button writeToSharedPref = (Button) inflatedView.findViewById(R.id.sendMessage);
        writeToSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });

    return inflatedView;
}

    public void sendMessage(View v) {
        // Do something in response to button
        Intent intent = new Intent(getActivity(),
                com.example.adamhurwitz.hackingenvironment.DisplayMessageActivity.class);
        /*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }

}

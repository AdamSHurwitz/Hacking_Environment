package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class LaunchNewActivityFragment extends Fragment {
    private final String LOG_TAG = LaunchNewActivityFragment.class.getSimpleName();

    //public static final String EXTRA_MESSAGE = "com.example.adamhurwitz.hackingenvironment.MainActivity";

    public LaunchNewActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.new_activity, container, false);

        setHasOptionsMenu(true);

        /**
         * Called when the user clicks the Send button
         */
        Button launchActivity = (Button) inflatedView.findViewById(R.id.launch_activity_btn);
        launchActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(v);
            }
        });

    return inflatedView;
}

    public void launchActivity(View v) {
        // Do something in response to button
        Intent intent = new Intent(getActivity(),
                LaunchedNewActivity.class);
        /*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*/
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_toast) {
            Context context = getActivity();
            CharSequence text = "MENU BUTTON!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

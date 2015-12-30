package com.example.adamhurwitz.hackingenvironment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class LaunchedNewActivity extends AppCompatActivity {
private final String LOG_TAG = LaunchedNewActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_launched);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Status Bar: Add Color - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar));

        // ----------------------------------------------------------------------------------------

        // Back Button To Go Home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        // receive the intent
        /*Intent intent = getIntent();
         if (intent != null) {
            String message = intent.getStringExtra(
                    com.example.adamhurwitz.hackingenvironment.LaunchNewActivityFragment.EXTRA_MESSAGE);
            // display the message
            TextView textView = new TextView(this);
            textView.setTextSize(40);
            textView.setText(message);
            setContentView(textView);
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_toast:
                Toast toast = Toast.makeText(this, "MENU BUTTON!", Toast.LENGTH_SHORT);
                toast.show();
                Log.v(LOG_TAG, "MENU CALLED: MENU BTN");
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                Toast toast2 = Toast.makeText(this, "SETTINGS", Toast.LENGTH_SHORT);
                toast2.show();
                Log.v(LOG_TAG, "MENU CALLED: SETTINGS");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

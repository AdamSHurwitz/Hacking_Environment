package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.adamhurwitz.hackingenv";

    // Inflate new Activity onClick in Activity

    /**
     * Called when the user clicks the Send button
     */
    /*public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this,
                com.example.adamhurwitz.hackingenvironment.DisplayMessageActivity.class);
        *//*EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);*//*
        startActivity(intent);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This loads viewPager for multiple tab views
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    // Add Fragments to the TabsAdapter, TabsAdapter recycles views
    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new NewActivityTestFragment(), "NewActivity");
        adapter.addFragment(new SharedPrefTestFragment(), "SharedPref");
        adapter.addFragment(new SQLiteTestFragment(), "SQLite");
        adapter.addFragment(new NewTestFragment(), "NewTest");
        adapter.addFragment(new NewTestFragment(), "NewTest");
        viewPager.setAdapter(adapter);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}





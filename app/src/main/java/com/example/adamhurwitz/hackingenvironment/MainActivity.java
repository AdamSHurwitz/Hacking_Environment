package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    // DrawerView: Create Strings Array and ListView

    // Create Array of Strings for DrawerView menu
    private String[] drawerItems;
    private DrawerLayout mDrawerLayout;
    // Create ListView to use to show DrawerView menu items
    private ListView mDrawerList;
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NavTabs: This loads viewPager for multiple tab views

       /* ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }*/
        // ----------------------------------------------------------------------------------------

        // DrawerView

        // get Array[] of items
        drawerItems = getResources().getStringArray(R.array.sample_array  );
        // get View of drawer_layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // get View of ListView id
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item_view, drawerItems));
        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // ----------------------------------------------------------------------------------------

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







package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.adamhurwitz.hackingenvironment.sync.SyncAdapter;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    // DrawerView: Initialize DrawerLayout - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private DrawerLayout mDrawerLayout;
    private final String LOADERFRAGMENT_TAG = "LFTAG";
    private String initialPrefString;
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(new SampleDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());


        // Get initial SharedPreference setting
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        initialPrefString = pref.getString("loader_settings_key", "popular");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CursorLoaderFragment(), LOADERFRAGMENT_TAG)
                    .commit();
        }

        // DrawerView: Initialize Toolbar and Navigation View, Set Listener - - - - - - - - - - - -

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation View
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Click Listeners for DrawerView Menu Items
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        // Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        // Closing navtabs on item click
                        mDrawerLayout.closeDrawers();

                        // Check to see which item was being clicked and perform appropriate action
                        switch (menuItem.getItemId()) {


                            //Replacing the main content with AddedFragmentWithinTab Which is our Inbox View;
                   /* case R.id.add_fragment:
                        Toast.makeText(getApplicationContext(), "ADD FRAGMENT", Toast.LENGTH_SHORT)
                                .show();
                        AddedFragmentWithinTab fragment = new AddedFragmentWithinTab();
                        android.support.v4.app.FragmentTransaction fragmentTransaction =
                                getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,fragment);
                        fragmentTransaction.commit();
                        return true;*/

                            // For rest of the options we just show a toast on click

                            case R.id.toast:
                                Toast.makeText(getApplicationContext(), "TOAST", Toast.LENGTH_SHORT).show();
                                return true;

                            case R.id.launch_activity:
                                Toast.makeText(getApplicationContext(), "LAUNCH ACTIVITY", Toast
                                        .LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),
                                        LaunchedNewActivity
                                                .class);
                                startActivity(intent);
                                return true;

                            //TODO: Enable Down/Up Navigation to Tab Fragments, savedBundleState(), onRestoreState()
                            //http://developer.android.com/training/basics/activity-lifecycle/recreating.html
                            case R.id.navigate_to_tab:
                                Toast.makeText(getApplicationContext(), "ADD NAVIGATION TO TAB", Toast
                                        .LENGTH_SHORT).show();
                                return true;

                            default:
                                Toast.makeText(getApplicationContext(), "Somethings Wrong",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                        }
                    }
                });

        // Initialize Drawer Layout and ActionBarToggle - - - - - - - - - - - - - - - - - - - - - -
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the navtabs closes as we dont want anything to
                // happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the navtabs open as we dont want anything to
                // happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to navtabs layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        // ----------------------------------------------------------------------------------------


        // NavTabs: This loads viewPager for multiple tab views - - - - - - - - - - - - - - - - - -

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }

        // ----------------------------------------------------------------------------------------

        SyncAdapter.initializeSyncAdapter(this);
    }


    // NavTabs: Add Fragments to the TabsAdapter, TabsAdapter recycles views - - - - - - - - - - - -
    private void setupViewPager(ViewPager viewPager) {
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        //adapter.addFragment(new PredefinedContentProviderFragment(), "Predefined ContentProvider");
        adapter.addFragment(new LaunchNewActivityFragment(), "NewActivity");
        adapter.addFragment(new SharedPrefTestFragment(), "SharedPref");
        adapter.addFragment(new SQLiteTestFragment(), "SQLite");
        adapter.addFragment(new AddedFragmentWithinTabFragment(), "Add Fragment");
        adapter.addFragment(new StaticArrayAdapterFragment(), "Static Array Adapter");
        adapter.addFragment(new AsyncParcelableFragment1(), "Async with Parcelable (1/2)");
        adapter.addFragment(new AsyncParcelableFragment2(), "Async with Parcelable (2/2)");
        adapter.addFragment(new AsyncCursorFragment1(), "Async with CursorAdapter (1/2)");
        adapter.addFragment(new AsyncCursorFragment2(), "Async with CursorAdapter (2/2)");
        adapter.addFragment(new ContentProviderFragment(), "ContentProvider Fragment");
        adapter.addFragment(new CursorLoaderFragment(), "Loader Fragment");
        adapter.addFragment(new ServiceFragment(), "Service Fragment");
        adapter.addFragment(new SyncAdapterFragment(), "SyncAdapter Fragment");
        adapter.addFragment(new StaticRecyclerViewFragment(), "Static RecyclerView");
        adapter.addFragment(new CursorRecyclerViewFragment(), "RecyclerView with Cursor");
        adapter.addFragment(new NewTestFragment(), "New Test Fragment");
        viewPager.setAdapter(adapter);
    }
    // ---------------------------------------------------------------------------------------------


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
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences currentPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentPrefString = currentPref.getString("loader_settings_key", "popular");
        // update the location in our second pane using the fragment manager
        if (currentPrefString != null && !currentPrefString.equals(initialPrefString)) {
            CursorLoaderFragment lf = (CursorLoaderFragment) getSupportFragmentManager().findFragmentByTag(
                    LOADERFRAGMENT_TAG);
            if (null != lf) {
                lf.onPreferenceChange(currentPrefString);
            }
            initialPrefString = currentPrefString;
        }
    }

    // Create class for Stetho
    private static class SampleDumperPluginsProvider implements DumperPluginsProvider {
        private final Context mContext;

        public SampleDumperPluginsProvider(Context context){mContext = context;}

        @Override
        public Iterable<DumperPlugin> get() {
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
                plugins.add(defaultPlugin);
            }
            //plugins.add(new SyncAdapterFragment());
            return plugins;
        }
    }
}







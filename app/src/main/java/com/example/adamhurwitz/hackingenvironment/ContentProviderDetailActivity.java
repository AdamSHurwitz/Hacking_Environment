package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

public class ContentProviderDetailActivity extends AppCompatActivity {
private final String LOG_TAG = ContentProviderDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contentprovider_detail_layout);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation View
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Status Bar: Add Color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar));
        //TODO: Navigation Back To Tab Fragment, savedBundleState(), onRestoreState()
        //http://developer.android.com/training/basics/activity-lifecycle/recreating.html
        // Back Button To Go Home
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        //getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

/**
 * Detail Activity for Static Recycler View
 */
public class StaticRecyclerViewDetailActivity extends AppCompatActivity {
    private final String LOG_TAG = StaticRecyclerViewDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staticrecycler_detail_layout);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Status Bar: Add Color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.status_bar));
        // Up Navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

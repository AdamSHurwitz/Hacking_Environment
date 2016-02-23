package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by adamhurwitz on 1/2/16.
 */
public class StaticRecyclerViewFragment extends Fragment {

    private final String LOG_TAG = StaticRecyclerViewFragment.class.getSimpleName();

    // build Arrays/ArrayLists for static data
    public final Integer[] imageData = {R.drawable.popular1, R.drawable.popular2, R.drawable.popular3,
            R.drawable.popular4, R.drawable.popular5, R.drawable.popular6, R.drawable.popular7,
            R.drawable.popular7, R.drawable.popular8, R.drawable.popular9, R.drawable.popular10,
            R.drawable.popular11, R.drawable.popular12, R.drawable.popular13, R.drawable.popular14,
            R.drawable.popular15, R.drawable.popular16, R.drawable.popular17, R.drawable.popular18};
    public final String[] stringData = {"popular1", "popular2", "popular3",
            "popular4", "popular5", "popular6", "popular7",
            "popular7", "popular8", "popular9", "popular10",
            "popular11", "popular12", "popular13", "popular14",
            "popular15", "popular16", "popular17", "popular18"};

    // create object for class
    public StaticRecyclerViewFragment() {
    }

    // Build RecyclerView and inflate parent layout view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.staticrecycler_layout, container, false);
        // set LinearLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(new StaticRecyclerViewAdapter(
                // Current context
                getActivity(),
                // ID of the view item layout, not needed since we get it in onCreateViewHolder
                //R.layout.staticrecycler_item_layout,
                // Data passing into ArrayAdapter
                imageData,
                stringData));
        return rv;
    }

    // used for selection of items in menu bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_toast:
                Toast toast = Toast.makeText(getActivity(), "MENU BUTTON!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

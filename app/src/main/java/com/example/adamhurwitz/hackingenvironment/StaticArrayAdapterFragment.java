package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class StaticArrayAdapterFragment extends Fragment {
    private final String LOG_TAG = StaticArrayAdapterFragment.class.getSimpleName();

    // Array of Images From Drawable Directory - - - - - - - - - - - - - - - - - - - - - - - - - - -
    public final Integer[] dummyData = {R.drawable.popular1,R.drawable.popular2,R.drawable.popular3,
            R.drawable.popular4,R.drawable.popular5,R.drawable.popular6,R.drawable.popular7,
            R.drawable.popular7,R.drawable.popular8,R.drawable.popular9,R.drawable.popular10,
            R.drawable.popular11,R.drawable.popular12,R.drawable.popular13,R.drawable.popular14,
            R.drawable.popular15,R.drawable.popular16,R.drawable.popular17,R.drawable.popular18};

    public StaticArrayAdapterFragment() {
    }

    // Create ArrayAdapter
    private ArrayAdapter dummyDataAdapter;

    // ArrayAdapter
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.static_view_layout, container, false);

        setHasOptionsMenu(true);

        dummyDataAdapter = new StaticArrayAdapter(
                // Current context (this fragment's containing activity)
                getActivity(),
                // ID of view item layout, not needed since we get it in getView()
                R.layout.static_item_layout,
                // ArrayAdapter
                dummyData
                );


        // Get a reference to GridView, and attach this adapter to it
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(dummyDataAdapter);
        return view;
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

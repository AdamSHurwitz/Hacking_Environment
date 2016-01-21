package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PredefinedContentProviderFragment extends Fragment {
    private final String LOG_TAG = PredefinedContentProviderFragment.class.getSimpleName();
    private static final String[] COLUMNS_TO_BE_BOUND = new String[]{
            UserDictionary.Words.WORD,
            UserDictionary.Words.FREQUENCY
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[]{
            android.R.id.text1,
            android.R.id.text2
    };

    /**
     * Empty constructor for the AsyncParcelableFragment1() class.
     */
    public PredefinedContentProviderFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.predefined_contentprovider_layout, container, false);

        ListView dictListView = (ListView) getActivity().findViewById(R.id.dictionary_list_view);

        // Get the ContentResolver which will send a message to the ContentProvider
        ContentResolver resolver = getActivity().getContentResolver();

        // Get a Cursor containing all of the rows in the Words table
        Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, null, null, null);
        Log.v(LOG_TAG, "Content_URI: " + UserDictionary.Words.CONTENT_URI);

        SimpleCursorAdapter sCursorAdapter = new SimpleCursorAdapter(getContext(),
                android.R.layout.two_line_list_item, cursor, COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL, 0);

        dictListView.setAdapter(sCursorAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_toast:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

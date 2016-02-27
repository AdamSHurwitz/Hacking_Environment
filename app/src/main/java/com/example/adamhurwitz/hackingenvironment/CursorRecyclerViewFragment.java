package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
import com.example.adamhurwitz.hackingenvironment.service.Service;

/**
 * A placeholder fragment containing a simple view.
 */
public class CursorRecyclerViewFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Empty constructor for the PopularFragment class.
     */
    public CursorRecyclerViewFragment() {
    }

    MyListCursorAdapter recyclerAdapter;

    private final String LOG_TAG = CursorRecyclerViewFragment.class.getSimpleName();
    private static final int LOADER_FRAGMENT = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDoodleData();

        RecyclerView rv = (RecyclerView) inflater.inflate(
                R.layout.recycler_main_layout, container, false);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // set GridLayoutManager
        rv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));

        Cursor cursor = getContext().getContentResolver().query(
                ContentProviderContract.ContentProviderProductData.CONTENT_URI,  // The table to query
                null, // The columns to return
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_VINTAGE + "= ? AND "
                        + ContentProviderContract.ContentProviderProductData.
                        COLUMN_NAME_RECENT + " = ? ", // The columns for the WHERE clause
                new String[]{"0", "0"},                            // The values for the WHERE clause
                ContentProviderContract.ContentProviderProductData._ID + " DESC"  // The sort order
        );
        Log.v(LOG_TAG, "setupRecyclerView() - cursor.getCount() " + cursor.getCount());


        recyclerAdapter = new MyListCursorAdapter(getActivity(), cursor);

        rv.setAdapter(recyclerAdapter);

        return rv;
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            getActivity().startService(new Intent(getContext(), Service.class)
                    .putExtra("service_extra", "item_id.desc"));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_FRAGMENT, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                ContentProviderContract.ContentProviderProductData.CONTENT_URI, null,
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_VINTAGE + " = ? AND "
                        + ContentProviderContract.ContentProviderProductData.
                        COLUMN_NAME_RECENT + " = ?",
                new String[] {"0","0"}, ContentProviderContract.ContentProviderProductData.
                COLUMN_NAME_POPULARITY + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        recyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        recyclerAdapter.swapCursor(null);
    }

}

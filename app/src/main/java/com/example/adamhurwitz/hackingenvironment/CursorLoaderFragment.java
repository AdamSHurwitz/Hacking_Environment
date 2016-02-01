package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
import com.example.adamhurwitz.hackingenvironment.data.CursorContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class CursorLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = CursorLoaderFragment.class.getSimpleName();
    private CursorLoaderAdapter cursorLoaderAdapter;
    public String showFilter = "";
    String doodleTitle = "";
    String doodleFavortie = "";
    private static final int LOADER_FRAGMENT = 0;
    //String initialPref;

    // How you want the results sorted in the resulting Cursor
    String whereColumns = CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND "
            + CursorContract.ProductData.COLUMN_NAME_RECENT + " = ?";
    String[] whereValues = {"0", "0"};
    String sortOrder = "";
    String filterBy;
    String mCurrentPref = "";

    /**
     * Empty constructor for the AsyncParcelableFragment1() class.
     */
    public CursorLoaderFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_loaderview_layout, container, false);

        setHasOptionsMenu(true);

        cursorLoaderAdapter = new CursorLoaderAdapter(getActivity(), null, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.grid_loaderview_layout);
        gridView.setAdapter(cursorLoaderAdapter);


        // Create Toast

        // gridView.setOnItemClickListener(new OnItem... [auto-completes])
        /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // parent = parent view, view = grid_item view, position = grid_item position
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // removed and replaced w/ Explicit Intent
                Toast.makeText(getActivity(), doodleDataList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ImageView favoriteButton = (ImageView) view.findViewById(
                        R.id.loadergridItem_favorite_button);
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String item_id = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_ITEMID));
                String title = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_TITLE));
                doodleTitle = title;
                String image = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_IMAGEURL));
                String description = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_PRICE));
                String release_date = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_RELEASEDATE));
                String favorite = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_FAVORITE));
                doodleFavortie = favorite;

                String[] doodleDataItems = {item_id, title, image, description, price, release_date,
                        favorite};

                Log.v(LOG_TAG, "Before Intent: " + favorite);

                Intent intent = new Intent(getActivity(),
                        ContentProviderDetailActivity.class);

                intent.putExtra("ContentProvider Doodle Attributes", doodleDataItems);

                startActivity(intent);

                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ContentValues values = new ContentValues();
                        int rowsUpdated = 0;
                        if (doodleFavortie.equals("1")) {
                            favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
                            values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 2);
                        } else {
                            favoriteButton.setImageResource(R.drawable.star_default_18dp);
                            values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 1);
                        }
                        rowsUpdated = getContext().getContentResolver().update(
                                ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                                values, ContentProviderContract.ContentProviderProductData
                                        .COLUMN_NAME_TITLE + "= ?", new String[]{doodleTitle});
                    }
                });
            }
        });

        Toast.makeText(getContext(), "Filtering" + showFilter + "...", Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cursorLoaderAdapter.notifyDataSetChanged();
        getData();
    }

    private void getData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Get SharedPref Value
      /*  SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String result = pref.getString("loader_settings_key", "popular");*/

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            /*ContentProviderFetchDataTask contentProviderTask = new ContentProviderTask(
                    getContext(), asyncCursorAdapter) {*/
            LoaderFetchDataTask loaderTask = new LoaderFetchDataTask(
                    getContext(), cursorLoaderAdapter) {
            };
            loaderTask.execute("item_id.desc");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER_FRAGMENT, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void showFilter(String filterBy) {
        showFilter = filterBy;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        filterBy = pref.getString("loader_settings_key", "popular");
        showFilter(filterBy);

        switch (filterBy) {
            case "popular":
                whereValues[0] = "0";
                whereValues[1] = "0";
                sortOrder = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_POPULARITY + " DESC";
                Log.v(LOG_TAG, "Filtering by filterBy - " + filterBy);
                break;
            case "recent":
                whereValues[0] = "0";
                whereValues[1] = "1";
                sortOrder = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_RELEASEDATE + " DESC";
                Log.v(LOG_TAG, "Filtering by filterBy - " + filterBy);
                break;
            case "vintage":
                whereValues[0] = "1";
                whereValues[1] = "0";
                sortOrder = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_RELEASEDATE + " DESC";
                Log.v(LOG_TAG, "Filtering by filterBy - " + filterBy);
                break;
            default:
                whereValues[0] = "0";
                whereValues[1] = "0";
                sortOrder = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_POPULARITY + " DESC";
                Log.v(LOG_TAG, "Filtering by filterBy - " + filterBy);
                break;
        }
        return new CursorLoader(getActivity(),
                ContentProviderContract.ContentProviderProductData.CONTENT_URI, null, whereColumns,
                whereValues, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursorLoaderAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        cursorLoaderAdapter.swapCursor(null);
    }

    // since we read the location when we create the loader, all we need to do is restart things
    public void onPreferenceChange(String currentPref) {
        mCurrentPref = currentPref;
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

    //TODO: Figure out how to update UI when Settings change
    @Override
    public void onResume() {
        super.onResume();
        String whereColumns2 = CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND "
                + CursorContract.ProductData.COLUMN_NAME_RECENT + " = ?";
        String[] whereValues2 = {"0", "0"};
        String sortOrder2 = "";
        Log.v(LOG_TAG, "onPreferenceChange() - " + mCurrentPref);
        //if (!mCurrentPref.equals(null)) {
        switch (mCurrentPref) {
            case "popular":
                whereValues2[0] = "0";
                whereValues2[1] = "0";
                sortOrder2 = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_POPULARITY + " DESC";
                Log.v(LOG_TAG, "Filtering by " + mCurrentPref);
                break;
            case "recent":
                whereValues2[0] = "0";
                whereValues2[1] = "1";
                sortOrder2 = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_RELEASEDATE + " DESC";
                Log.v(LOG_TAG, "Filtering by " + mCurrentPref);
                break;
            case "vintage":
                whereValues2[0] = "1";
                whereValues2[1] = "0";
                sortOrder2 = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_RELEASEDATE + " DESC";
                Log.v(LOG_TAG, "Filtering by " + mCurrentPref);
                break;
            default:
                whereValues2[0] = "0";
                whereValues2[1] = "0";
                sortOrder2 = ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_POPULARITY + " DESC";
                Log.v(LOG_TAG, "Filtering by " + mCurrentPref);
                break;
        }

        Cursor onResumeCursor = getContext().getContentResolver().query(
                // The table to query
                ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                // The columns to return
                null,
                // The columns for the WHERE clause
                whereColumns2,
                // The values for the WHERE clause
                whereValues2,
                // The sort order
                sortOrder2);
        cursorLoaderAdapter.changeCursor(onResumeCursor);
        //}
    }
}


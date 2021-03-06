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
public class ContentProviderFragment extends Fragment {
    private final String LOG_TAG = ContentProviderFragment.class.getSimpleName();
    private ContentProviderCursorAdapter contentProviderCursorAdapter;
    public String showFilter = "";
    String doodleTitle = "";
    String doodleFavortie = "";

    /**
     * Empty constructor for the AsyncParcelableFragment1() class.
     */
    public ContentProviderFragment() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_contentproviderview_layout, container, false);

        setHasOptionsMenu(true);

        contentProviderCursorAdapter = new ContentProviderCursorAdapter(getActivity(), null, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.grid_contentproviderview_layout);
        gridView.setAdapter(contentProviderCursorAdapter);


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
                        R.id.gridItem_favorite_button);
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
                                        .COLUMN_NAME_TITLE + "= ?", new String[] {doodleTitle});
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
        contentProviderCursorAdapter.notifyDataSetChanged();
        getData();
    }

    private void getData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Get SharedPref Value
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String result = pref.getString("asynccursor2_settings_key",
                "popular");

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            /*ContentProviderFetchDataTask contentProviderTask = new ContentProviderTask(
                    getContext(), asyncCursorAdapter) {*/
            ContentProviderFetchDataTask contentProviderTask = new ContentProviderFetchDataTask(
                    getContext(), contentProviderCursorAdapter) {
            };
            contentProviderTask.execute("item_id.desc");
        }
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

package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.adamhurwitz.hackingenvironment.data.CursorContract;
import com.example.adamhurwitz.hackingenvironment.data.CursorDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class AsyncCursorFragment1 extends Fragment {

    private AsyncCursorAdapter asyncCursorAdapter;
    public String showFilter = "";

    /**
     * Empty constructor for the AsyncParcelableFragment1() class.
     */
    public AsyncCursorFragment1() {
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_recentview_layout, container, false);

        setHasOptionsMenu(true);

        asyncCursorAdapter = new AsyncCursorAdapter(getActivity(), null, 0);

        // Get a reference to the grid view layout and attach the adapter to it.
        GridView gridView = (GridView) view.findViewById(R.id.grid_recentview_layout);
        gridView.setAdapter(asyncCursorAdapter);


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
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                String item_id = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_ITEMID));
                String title = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_TITLE));
                String image = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_IMAGEURL));
                String description = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_DESCRIPTION));
                String price = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_PRICE));
                String release_date = cursor.getString(cursor.getColumnIndex(CursorContract.ProductData
                        .COLUMN_NAME_RELEASEDATE));

                String[] doodleDataItems = {item_id, title, image, description, price, release_date};

                Intent intent = new Intent(getActivity(),
                        AsyncCursorDetailActivity.class);

                intent.putExtra("Cursor Doodle Attributes", doodleDataItems);

                startActivity(intent);
            }
        });

        Toast.makeText(getContext(),"Filtering by " + showFilter +"...",Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncCursorAdapter.notifyDataSetChanged();
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
            AsyncCursorFetchDataTask doodleTask = new AsyncCursorTask2(getContext());
            doodleTask.execute("item_id.desc");

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

    public void showFilter(String filterBy){
        showFilter = filterBy;
    }

    private class AsyncCursorTask2 extends AsyncCursorFetchDataTask {

        /**
         * Constructor for the AsyncParcelableFetchDoodleDataTask object.
         *
         * @param context Context of Activity
         */
        public AsyncCursorTask2(Context context) {
            super(context);
        }

        @Override
        public void onPostExecute(Void param) {
            // Access database
            CursorDbHelper mDbHelper = new CursorDbHelper(getContext());
            // Gets the data repository in read mode
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            // The columns for the WHERE clause
            String whereColumns = "";
            // The values for the WHERE clause
            String[] whereValues = {"0","0"};
            // How you want the results sorted in the resulting Cursor
            String sortOrder = "";

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String filterBy = pref.getString("asynccursor1_settings_key", "popular");
            whereColumns = CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND "
                    + CursorContract.ProductData.COLUMN_NAME_RECENT + " = ?";

            showFilter(filterBy);

            switch (filterBy){
                case "popular":
                    whereValues[0] = "0";
                    whereValues[1] = "0";
                    sortOrder = CursorContract.ProductData.COLUMN_NAME_POPULARITY + " DESC";
                    break;
                case "recent":
                    whereValues[0] = "0";
                    whereValues[1] = "1";
                    sortOrder = CursorContract.ProductData.COLUMN_NAME_RELEASEDATE + " DESC";
                    /*Toast.makeText(getContext(),"Filtering by " + filterBy+"...", Toast.LENGTH_SHORT)
                            .show();*/
                    break;
                case "vintage":
                    whereValues[0] = "1";
                    whereValues[1] = "0";
                    sortOrder = CursorContract.ProductData.COLUMN_NAME_RELEASEDATE + " DESC";
                    Toast.makeText(getContext(),"Filtering by " + filterBy+"...", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    whereValues[0] = "0";
                    whereValues[1] = "0";
                    sortOrder = CursorContract.ProductData.COLUMN_NAME_POPULARITY + " DESC";
                    /*Toast.makeText(getContext(),"Filtering by " + filterBy +"...", Toast.LENGTH_SHORT)
                            .show();*/
                    break;
            }
            Cursor cursor = db.query(
                    CursorContract.ProductData.TABLE_NAME,  // The table to query
                    null,                               // The columns to return
                    whereColumns,  // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            asyncCursorAdapter.changeCursor(cursor);
            asyncCursorAdapter.notifyDataSetChanged();
        }
    }

}

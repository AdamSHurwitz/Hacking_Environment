package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

        // Access database
        CursorDbHelper mDbHelper = new CursorDbHelper(getContext());
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CursorContract.ProductData._ID + " DESC";
        String[] wherevalues = {"1"};

        // If you are querying entire table, can leave everything as Null
        Cursor cursor = db.query(
                CursorContract.ProductData.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ?",  // The columns for the WHERE clause
                wherevalues,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        asyncCursorAdapter = new AsyncCursorAdapter(getActivity(), cursor, 0);

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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        asyncCursorAdapter.notifyDataSetChanged();
        getDoodleData();
    }

    private void getDoodleData() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        // Make sure that the device is actually connected to the internet before trying to get data
        // about the Google doodles.
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            AsyncCursorFetchDoodleDataTask doodleTask = new AsyncCursorFetchDoodleDataTask(asyncCursorAdapter,
                    getContext());
            doodleTask.execute("release_date.desc", "vintage");

        }
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

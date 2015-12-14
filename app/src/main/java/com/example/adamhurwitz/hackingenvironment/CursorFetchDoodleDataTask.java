package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.adamhurwitz.hackingenvironment.data.CursorContract;
import com.example.adamhurwitz.hackingenvironment.data.CursorDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adamhurwitz on 12/2/15.
 */
public class CursorFetchDoodleDataTask extends AsyncTask<String, Void, Void> {

    public static final String LOG_TAG = CursorFetchDoodleDataTask.class.getSimpleName();
    public static final String FAS_API_BASE_URL = "https://fas-api.appspot.com/";
    public static final String SORT_PARAMETER = "sort_order";
    public static final String ID_PARAMETER = "item_id";
    public static final String TITLE_PARAMETER = "title";
    public static final String RELEASE_DATE_PARAMETER = "release_date";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String SEARCH_STRINGS = "search_strings";
    public static final String PRICE_PARAMETER = "price";
    public static final String IMAGE_URL_PARAMETER = "image_url";
    public static final String POPULARITY = "popularity";
    public static final String IS_RECENT_BOOLEAN = "recent";
    public static final String IS_VINTAGE_BOOLEAN = "vintage";

    private GridViewCursorAdapter gridViewCursorAdapter;
    private final Context context;

    /**
     * Constructor for the FetchDoodleDataTask object.
     *
     * @param gridViewCursorAdapter An adapter to recycle items correctly in the grid view.
     * @param context               Context of Activity
     */
    public CursorFetchDoodleDataTask(GridViewCursorAdapter gridViewCursorAdapter, Context context) {
        this.gridViewCursorAdapter = gridViewCursorAdapter;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // This variable will contain the raw JSON response as a string.
        String doodleDataJsonResponse = null;

        try {
            // Construct the URL to fetch data from and make the connection.
            Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAMETER, params[0])
                            //.appendQueryParameter(IS_RECENT_BOOLEAN, params[1])
                    .appendQueryParameter(params[1], "true")
                    .build();
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // See if the input stream is not null and a connection could be made. If it is null, do
            // not process any further.
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }

            // Read the input stream to see if any valid response was give.
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Add new to make debugging easier.
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                // If the stream is empty, do not process any further.
                return null;
            }

            doodleDataJsonResponse = buffer.toString();

        } catch (IOException e) {
            // If there was no valid Google doodle data returned, there is no point in attempting to
            // parse it.
            Log.e(LOG_TAG, "Error, IOException.", e);
            return null;
        } finally {
            // Make sure to close the connection and the reader no matter what.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream ", e);
                }
            }
        }

        // If valid data was returned, return the parsed data.
        try {
            Log.i(LOG_TAG, "The Google doodle data that was returned is: " +
                    doodleDataJsonResponse);
            parseDoodleDataJsonResponse(doodleDataJsonResponse);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // Any other case that gets here is an error that was not caught, so return null.
        return null;
    }

    @Override
    /**
     * Override the onPostExecute method to notify the grid view adapter that new data was received
     * so that the items in the grid view can appropriately reflect the changes.
     * @param doodleData A list of objects with information about the Google doodles.
     */
    public void onPostExecute(Void param) {
        gridViewCursorAdapter.notifyDataSetChanged();
    }

    /**
     * Parses the JSON response for information about the Google doodles.
     *
     * @param doodleDataJsonResponse A JSON string which needs to be parsed for data about the
     *                               Google doodles.
     */
    private void parseDoodleDataJsonResponse(String doodleDataJsonResponse)
            throws JSONException {

        try {
            JSONArray doodlesInfo = new JSONArray(doodleDataJsonResponse);
            for (int index = 0; index < doodlesInfo.length(); index++) {
                JSONObject doodleDataJson = doodlesInfo.getJSONObject(index);
                putDoodleDataIntoDb(
                        doodleDataJson.getString(ID_PARAMETER),
                        doodleDataJson.getString(TITLE_PARAMETER),
                        doodleDataJson.getString(RELEASE_DATE_PARAMETER),
                        doodleDataJson.getString(DESCRIPTION_PARAMETER),
                        doodleDataJson.getString(SEARCH_STRINGS),
                        doodleDataJson.getInt(PRICE_PARAMETER),
                        doodleDataJson.getString(IMAGE_URL_PARAMETER),
                        doodleDataJson.getDouble(POPULARITY),
                        doodleDataJson.getBoolean(IS_RECENT_BOOLEAN),
                        doodleDataJson.getBoolean(IS_VINTAGE_BOOLEAN));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void putDoodleDataIntoDb(String item_id, String title, String date, String description,
                                    String search_strings, int price, String image,
                                    Double popularity, Boolean recent, Boolean vintage) {
        Log.v("putInfoIntoDatabase", "called here");

        // Access database
        CursorDbHelper mDbHelper = new CursorDbHelper(context);

        // Put Info into Database

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CursorContract.ProductData.COLUMN_NAME_ITEMID, item_id);
        values.put(CursorContract.ProductData.COLUMN_NAME_TITLE, title);
        values.put(CursorContract.ProductData.COLUMN_NAME_RELEASEDATE, date);
        values.put(CursorContract.ProductData.COLUMN_NAME_DESCRIPTION, description);
        values.put(CursorContract.ProductData.COLUMN_NAME_SEARCHSTRINGS, search_strings);
        values.put(CursorContract.ProductData.COLUMN_NAME_PRICE, price);
        values.put(CursorContract.ProductData.COLUMN_NAME_IMAGEURL, image);
        values.put(CursorContract.ProductData.COLUMN_NAME_DESCRIPTION, description);
        values.put(CursorContract.ProductData.COLUMN_NAME_POPULARITY, popularity);
        values.put(CursorContract.ProductData.COLUMN_NAME_RECENT, recent);
        values.put(CursorContract.ProductData.COLUMN_NAME_VINTAGE, vintage);

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CursorContract.ProductData._ID + " DESC";
        String whereValue[] = {item_id};

        // Insert the new row, returning the primary key value of the new row
        long thisRowID;

        // If you are querying entire table, can leave everything as Null
        // Querying when Item ID Exists
        Cursor cursor = db.query(
                CursorContract.ProductData.TABLE_NAME,  // The table to query
                null,                                // The columns to return
                CursorContract.ProductData.COLUMN_NAME_ITEMID + "= ?", // The columns for the WHERE clause
                whereValue, // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        // If the Item ID Does Not Exist, Insert All Values
        if (cursor.getCount() == 0) {
            thisRowID = db.insert(
                    CursorContract.ProductData.TABLE_NAME,
                    null,
                    values);
        }

        // If the Item ID Does Exist, Update All Values
        else {
            thisRowID = db.update(
                    CursorContract.ProductData.TABLE_NAME,
                    values,
                    CursorContract.ProductData.COLUMN_NAME_ITEMID + "= ?",
                    whereValue);
        }


    }
}


package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
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
import java.util.Vector;

/**
 * Created by adamhurwitz on 12/2/15.
 */

// param1 passes into doInBackground()
// param3 declares return type for doInBackground()
public abstract class ContentProviderFetchDataTask extends AsyncTask<String, Void, Void> {

    public static final String LOG_TAG = ContentProviderFetchDataTask.class.getSimpleName();
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

    private final Context context;
    private AsyncCursorAdapter asyncCursorAdapter;
    public String showFilter = "";
    Vector<ContentValues> cVVector;


    /**
     * Constructor for the AsyncParcelableFetchDoodleDataTask object.
     *
     * @param context Context of Activity
     */
    public ContentProviderFetchDataTask(Context context, AsyncCursorAdapter cursorAdapter) {
        this.context = context;
        asyncCursorAdapter = cursorAdapter;
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // This variable will contain the raw JSON response as a string.
        String jsonResponse = null;

        //TODO: Use As Example For Switch Case Based on Shared Preferences
        try {
            // Construct the URL to fetch data from and make the connection.
            Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAMETER, params[0])
                    .build();
            URL url = new URL(builtUri.toString());
            Log.v("REC/VINT_URL_HERE: ", builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /*if (params[1] != "popular") {
                // Construct the URL to fetch data from and make the connection.
                Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAMETER, params[0])
                        .appendQueryParameter(params[1], "true")
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("REC/VINT_URL_HERE: ", builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            } else {    // Construct the URL to fetch data from and make the connection.
                Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAMETER, params[0])
                        .appendQueryParameter(params[1], "true")
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("POPULAR_URL_HERE: ", builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }*/

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

            jsonResponse = buffer.toString();
            Log.v("JSON_String_here: ", jsonResponse);

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
                    jsonResponse);
            parseJSONResponse(jsonResponse);
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // Any other case that gets here is an error that was not caught, so return null.
        return null;
    }

    /**
     * Parses the JSON response for information about the Google doodles.
     *
     * @param jsonResponse A JSON string which needs to be parsed for data about the
     *                     Google doodles.
     */
    private void parseJSONResponse(String jsonResponse)
            throws JSONException {

        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);

            // Initialize ArrayList of Content Values size of data Array length
            cVVector = new Vector<>(jsonarray.length());

            for (int index = 0; index < jsonarray.length(); index++) {
                JSONObject jsonObject = jsonarray.getJSONObject(index);
                putDataIntoDb(
                        jsonObject.getString(ID_PARAMETER),
                        jsonObject.getString(TITLE_PARAMETER),
                        jsonObject.getString(RELEASE_DATE_PARAMETER),
                        jsonObject.getString(DESCRIPTION_PARAMETER),
                        jsonObject.getString(SEARCH_STRINGS),
                        jsonObject.getInt(PRICE_PARAMETER),
                        jsonObject.getString(IMAGE_URL_PARAMETER),
                        jsonObject.getDouble(POPULARITY),
                        jsonObject.getBoolean(IS_RECENT_BOOLEAN),
                        jsonObject.getBoolean(IS_VINTAGE_BOOLEAN));
                Log.v("JSON_RESPONSE", jsonResponse);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void putDataIntoDb(String item_id, String title, String date, String description,
                              String search_strings, int price, String image,
                              Double popularity, Boolean recent, Boolean vintage) {
        Log.v("ALPHA", title);

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
        values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, "1");

        cVVector.add(values);

        //if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(ContentProviderContract
                            .ContentProviderProductData.CONTENT_URI, cvArray);
        //}

        Log.v(LOG_TAG, "Length_of_Vector: " + cVVector.size());

        //Log.v(LOG_TAG, "Bulk Insert: " + cVVector.size());

        /*Log.v(LOG_TAG, "Content Values " + values.toString());

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CursorContract.ProductData._ID + " DESC";
        String[] whereValueId = {item_id};
        String[] whereValueTitle = {title};

        // Insert the new row, returning the primary key value of the new row
        long thisRowID;

        // If you are querying entire table, can leave everything as Null
        // Querying when Item ID Exists
        Cursor cursor = db.query(
                CursorContract.ProductData.TABLE_NAME,  // The table to query
                null,                                // The columns to return
                CursorContract.ProductData.COLUMN_NAME_TITLE + "= ?", // The columns for the WHERE clause
                whereValueTitle, // The values for the WHERE clause
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
        }*/


        // Example of DB Query and Insert using ContentProvider - - - - - - - - - - - - - - - - - - - -

    /* long addLocation(String locationSetting, String cityName, double lat, double lon) {
        long locationId;

        // First, check if the location with this city name exists in the db
        Cursor locationCursor = mContext.getContentResolver().query(
                WeatherContract.LocationEntry.CONTENT_URI,
                new String[]{WeatherContract.LocationEntry._ID},
                WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null);

        if (locationCursor.moveToFirst()) {
            int locationIdIndex = locationCursor.getColumnIndex(WeatherContract.LocationEntry._ID);
            locationId = locationCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues locationValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            locationValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, lon);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    WeatherContract.LocationEntry.CONTENT_URI,
                    locationValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            locationId = ContentUris.parseId(insertedUri);
        }

        locationCursor.close();
        // Wait, that worked?  Yes!
        return locationId;
    } */

        // Example of Bulk Insert - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /*if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, cvArray);
        }*/

    }

    public void showFilter(String filterBy) {
        showFilter = filterBy;
    }

    @Override
    public void onPostExecute(Void param) {
        // Access database
        CursorDbHelper mDbHelper = new CursorDbHelper(context);
        // Gets the data repository in read mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // The columns for the WHERE clause
        String whereColumns = "";
        // The values for the WHERE clause
        String[] whereValues = {"0", "0"};
        // How you want the results sorted in the resulting Cursor
        String sortOrder = "";

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String filterBy = pref.getString("asynccursor1_settings_key", "popular");
        whereColumns = CursorContract.ProductData.COLUMN_NAME_VINTAGE + " = ? AND "
                + CursorContract.ProductData.COLUMN_NAME_RECENT + " = ?";

        showFilter(filterBy);

        switch (filterBy) {
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
                /*Toast.makeText(getContext(), "Filtering by " + filterBy + "...", Toast.LENGTH_SHORT)
                        .show();*/
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


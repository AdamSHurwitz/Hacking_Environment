package com.example.adamhurwitz.hackingenvironment.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.adamhurwitz.hackingenvironment.CursorLoaderAdapter;
import com.example.adamhurwitz.hackingenvironment.LoaderFetchDataTask;
import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
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
 * Created by adamhurwitz on 1/17/16.
 */
public class Service extends IntentService {
    public Service() {
        super("Service");
    }

    public static final String LOG_TAG = LoaderFetchDataTask.class.getSimpleName();
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

    private CursorLoaderAdapter cursorLoaderAdapter;
    public String showFilter = "";
    Vector<ContentValues> cVVector;

    @Override
    protected void onHandleIntent(Intent intent) {
        String param = intent.getStringExtra("service_extra");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // This variable will contain the raw JSON response as a string.
        String jsonResponse = null;

        try {
            // Construct the URL to fetch data from and make the connection.
            Uri builtUri = Uri.parse(FAS_API_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAMETER, param)
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
                return;
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
                return;
            }

            jsonResponse = buffer.toString();
            Log.v("JSON_String_here: ", jsonResponse);

        } catch (IOException e) {
            // If there was no valid Google doodle data returned, there is no point in attempting to
            // parse it.
            Log.e(LOG_TAG, "Error, IOException.", e);
            return;
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
            return;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // Any other case that gets here is an error that was not caught, so return null.
        return;
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

        // Access database
        CursorDbHelper mDbHelper = new CursorDbHelper(this);

        // Put Info into Database

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_ITEMID, item_id);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_TITLE, title);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_RELEASEDATE,
                date);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_DESCRIPTION,
                description);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_SEARCHSTRINGS,
                search_strings);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_PRICE, price);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_IMAGEURL, image);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_DESCRIPTION,
                description);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_POPULARITY,
                popularity);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_RECENT, recent);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_VINTAGE, vintage);
        values.put(ContentProviderContract.ContentProviderProductData.COLUMN_NAME_FAVORITE, "1");

        cVVector.add(values);

        // Queries the user dictionary and returns results
        Cursor cursor = this.getContentResolver().query(
                // The content URI of the words table
                ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                // projection: the columns to return for each row
                null,
                // selectionClause: selection criteria
                ContentProviderContract.ContentProviderProductData.COLUMN_NAME_TITLE + "= ?",
                // selectionArgs: selection criteria
                new String[]{title},
                // sortOrder: the sort order for the returned rows
                ContentProviderContract.ContentProviderProductData._ID);
        if (cursor.getCount() == 0) {
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                this.getContentResolver().bulkInsert(ContentProviderContract
                        .ContentProviderProductData.CONTENT_URI, cvArray);
            }
            Log.v(LOG_TAG, "Length_of_Vector: " + cVVector.size());
        }
    }

}


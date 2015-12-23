package com.example.adamhurwitz.hackingenvironment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by adamhurwitz on 12/2/15.
 */
public class FetchDoodleDataTask extends AsyncTask<String, Void, ArrayList<DoodleData>> {

    public static final String LOG_TAG = FetchDoodleDataTask.class.getSimpleName();
    public static final String FAS_API_BASE_URL = "https://fas-api.appspot.com/";
    public static final String SORT_PARAMETER = "sort_order";
    public static final String ID_PARAMETER = "item_id";
    public static final String TITLE_PARAMETER = "title";
    public static final String RELEASE_DATE_PARAMETER = "release_date";
    public static final String DESCRIPTION_PARAMETER = "description";
    public static final String PRICE_PARAMETER = "price";
    public static final String IMAGE_URL_PARAMETER = "image_url";
    public static final String IS_RECENT_BOOLEAN = "recent";

    private ArrayList<DoodleData> doodleDataList = new ArrayList<>();
    private AsyncParcelableArrayAdapter gridViewAdapter;

    /**
     * Constructor for the FetchDoodleDataTask object.
     * @param gridViewAdapter An adapter to recycle items correctly in the grid view.
     * @param doodleDataList A list of objects with information about Google doodles.
     */
    public FetchDoodleDataTask(AsyncParcelableArrayAdapter gridViewAdapter,
                               ArrayList<DoodleData> doodleDataList) {
        this.doodleDataList = doodleDataList;
        this.gridViewAdapter = gridViewAdapter;
    }

    @Override
    protected ArrayList<DoodleData> doInBackground(String... params) {

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
            return parseDoodleDataJsonResponse(doodleDataJsonResponse);
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
    public void onPostExecute(ArrayList<DoodleData> doodleData) {
        gridViewAdapter.notifyDataSetChanged();
    }

    /**
     * Parses the JSON response for information about the Google doodles.
     * @param doodleDataJsonResponse A JSON string which needs to be parsed for data about the
     *                               Google doodles.
     */
    private ArrayList<DoodleData> parseDoodleDataJsonResponse(String doodleDataJsonResponse)
            throws JSONException {
        try {
            JSONArray doodlesInfo = new JSONArray(doodleDataJsonResponse);
            for (int index = 0; index < doodlesInfo.length(); index++) {
                JSONObject doodleDataJson = doodlesInfo.getJSONObject(index);
                DoodleData doodleData = new DoodleData(doodleDataJson.getString(ID_PARAMETER),
                        doodleDataJson.getString(TITLE_PARAMETER),
                        doodleDataJson.getString(RELEASE_DATE_PARAMETER),
                        doodleDataJson.getString(DESCRIPTION_PARAMETER),
                        doodleDataJson.getString(PRICE_PARAMETER),
                        doodleDataJson.getString(IMAGE_URL_PARAMETER),
                        doodleDataJson.getBoolean(IS_RECENT_BOOLEAN));
                doodleDataList.add(doodleData);
            }
            return doodleDataList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

}


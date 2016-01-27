package com.example.adamhurwitz.hackingenvironment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
import com.example.adamhurwitz.hackingenvironment.data.CursorContract;
import com.squareup.picasso.Picasso;

/**
 * Created by adamhurwitz on 12/5/15.
 */
public class ContentProviderDetailFragment extends Fragment {


    public ContentProviderDetailFragment() {
    }

    String toggle = "off";

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_contentprovider_layout, container, false);

        // get id for favorite_btn
        final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id
                .contentprovider_favorite_btn);
        String favVal = "";

        //receive the intent
        //Activity has intent, must get intent from Activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            final String[] doodleDataElements = intent
                    .getStringArrayExtra("ContentProvider Doodle Attributes");
            // doodleDataElements[0] = item_id
            // doodleDataElements[1] = title
            // doodleDataElements[2] = image
            // doodleDataElements[3] = description
            // doodleDataElements[4] = price
            // doodleDataElements[5] = release_date
            // doodleDataElements[6] = favorite

            // Create DoodleData Within 'detail_fragment_layout.xml'
            ImageView doodle_image = (ImageView) view.findViewById(R.id
                    .contentprovider_detail_doodle_image);

            // generate images with Picasso
            // switch this to getActivity() since must get Context from Activity
            Picasso.with(getActivity())
                    .load(doodleDataElements[2])
                    .resize(1200, 500)
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(doodle_image);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView title = (TextView) view.findViewById(R.id.contentprovider_detail_title);
            title.setText(doodleDataElements[1]);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView price = (TextView) view.findViewById(R.id.contentprovider_detail_price);
            price.setText("$" + doodleDataElements[4]);

            //Create MovieData User Release Date Within 'fragment_detail.xml'
            TextView releaseDate = (TextView) view.findViewById(R.id
                    .contentprovider_detail_releasedate);
            releaseDate.setText("Released: " + doodleDataElements[5]);

            //Create MovieData User Rating Within 'fragment_detail.xml'
            TextView about = (TextView) view.findViewById(R.id.contentprovider_detail_description);
            about.setText(doodleDataElements[3]);

            //Handle favorite btn on view load

            Cursor cursor = getContext().getContentResolver().query(
                    ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                    new String[]{ContentProviderContract.ContentProviderProductData
                            .COLUMN_NAME_FAVORITE},
                    ContentProviderContract.ContentProviderProductData
                            .COLUMN_NAME_TITLE + "= ?",
                    new String[]{doodleDataElements[1]},
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                favVal = cursor.getString(
                        cursor.getColumnIndexOrThrow(CursorContract.ProductData
                                .COLUMN_NAME_FAVORITE));
            }

            //Logic to handle Favorite Button status on load
            if (favVal.equals("1")) {
                favoriteButton.setImageResource(R.drawable.star_default_18dp);
            } else {
                favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
            }

            // Click listener for favorite button
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    // Turn button on
                    if (doodleDataElements[6].equals("1")) {
                        //toggle = "on";
                        favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
                        ContentValues values = new ContentValues();
                        values.put(ContentProviderContract.ContentProviderProductData
                                .COLUMN_NAME_FAVORITE, "2");
                        int rowsUpdated = 0;
                        rowsUpdated = getContext().getContentResolver().update(
                                ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                                values, ContentProviderContract.ContentProviderProductData
                                        .COLUMN_NAME_TITLE + "= ?",
                                new String[]{doodleDataElements[1]});
                        doodleDataElements[6] = "2";
                    }
                    // Turn button off
                    else if (doodleDataElements[6].equals("2")) {
                        //toggle = "off";
                        favoriteButton.setImageResource(R.drawable.star_default_18dp);
                        ContentValues values = new ContentValues();
                        values.put(ContentProviderContract.ContentProviderProductData
                                .COLUMN_NAME_FAVORITE, "1");
                        int rowsUpdated = 0;
                        rowsUpdated = getContext().getContentResolver().update(
                                ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                                values, ContentProviderContract.ContentProviderProductData
                                        .COLUMN_NAME_TITLE + "= ?",
                                new String[]{doodleDataElements[1]});
                        doodleDataElements[6] = "1";
                    }
                }
            });

        }
        return view;
    }

}

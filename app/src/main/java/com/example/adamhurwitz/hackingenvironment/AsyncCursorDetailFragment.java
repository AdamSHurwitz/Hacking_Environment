package com.example.adamhurwitz.hackingenvironment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by adamhurwitz on 12/5/15.
 */
public class AsyncCursorDetailFragment extends Fragment {


    public AsyncCursorDetailFragment() {
    }

    String toggle = "off";

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);

        // get id for favorite_btn
        final ImageButton favoriteButton = (ImageButton) view.findViewById(R.id.favorite_btn);

        //receive the intent
        //Activity has intent, must get intent from Activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            String[] doodleDataElements = intent.getStringArrayExtra("Cursor Doodle Attributes");
            // doodleDataElements[0] = item_id
            // doodleDataElements[1] = title
            // doodleDataElements[2] = image
            // doodleDataElements[3] = description
            // doodleDataElements[4] = price
            // doodleDataElements[5] = release_date


            // Create DoodleData Within 'detail_fragment_layout.xml'
            ImageView doodle_image = (ImageView) view.findViewById(R.id.detail_doodle_image);

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
            TextView title = (TextView) view.findViewById(R.id.detail_title);
            title.setText(doodleDataElements[1]);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView price = (TextView) view.findViewById(R.id.detail_price);
            price.setText("$" + doodleDataElements[4]);

            //Create MovieData User Release Date Within 'fragment_detail.xml'
            TextView releaseDate = (TextView) view.findViewById(R.id.detail_releasedate);
            releaseDate.setText("Released: " + doodleDataElements[5]);

            //Create MovieData User Rating Within 'fragment_detail.xml'
            TextView about = (TextView) view.findViewById(R.id.detail_description);
            about.setText(doodleDataElements[3]);

            // Click listener for favorite button
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    // Turn button on
                    if (toggle.equals("off")) {
                        toggle = "on";
                        favoriteButton.setImageResource(R.drawable.star_pressed_18dp);
                        Toast.makeText(getContext(),toggle,Toast.LENGTH_SHORT).show();
                    }
                    // Turn button off
                    else if (toggle.equals("on")) {
                        toggle = "off";
                        favoriteButton.setImageResource(R.drawable.star_default_18dp);
                        Toast.makeText(getContext(),toggle,Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        return view;
    }

}

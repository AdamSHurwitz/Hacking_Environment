package com.example.adamhurwitz.hackingenvironment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by adamhurwitz on 12/5/15.
 */
public class DetailFragment extends Fragment {


    public DetailFragment() {
    }

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        //receive the intent
        //Activity has intent, must get intent from Activity
        Intent intent = getActivity().getIntent();
        if (intent != null) {

            DoodleData doodleData = intent.getParcelableExtra("Doodle Object");

            // Create DoodleData Within 'detail_fragment_layout.xml'
            ImageView doodle_image = (ImageView) view.findViewById(R.id.detail_doodle_image);

            final String imageUrl = doodleData.getImageUrl();

            // generate images with Picasso
            // switch this to getActivity() since must get Context from Activity
            Picasso.with(getActivity())
                    .load(imageUrl)
                    .resize(1300, 500)
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(doodle_image);

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView title = (TextView) view.findViewById(R.id.detail_title);
            title.setText(doodleData.getTitle());

            //Create Doodle Title within 'detail_fragment_layout.xml'
            TextView price = (TextView) view.findViewById(R.id.detail_price);
            price.setText("$" + doodleData.getPrice());

            //Create MovieData User Release Date Within 'fragment_detail.xml'
            TextView releaseDate = (TextView) view.findViewById(R.id.detail_releasedate);
            releaseDate.setText("Released: " + doodleData.getReleaseDate());

            //Create MovieData User Rating Within 'fragment_detail.xml'
            TextView about = (TextView) view.findViewById(R.id.detail_description);
            about.setText(doodleData.getDescription());

        }
        return view;
    }

}

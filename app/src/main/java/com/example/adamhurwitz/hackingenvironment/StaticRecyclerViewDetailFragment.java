package com.example.adamhurwitz.hackingenvironment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * * Detail Fragment for Static Recycler View
 */
public class StaticRecyclerViewDetailFragment extends Fragment {
    private final String LOG_TAG = StaticRecyclerViewDetailFragment.class.getSimpleName();

    public StaticRecyclerViewDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View inflatedView = inflater.inflate(R.layout.staticrecycler_detailfragment_layout,
                container, false);

        Intent intent = getActivity().getIntent();
        final String detailText = intent.getStringExtra("StaticRecylerViewExtra");

        TextView detailTextView = (TextView) inflatedView.findViewById(R.id.detailText);
        detailTextView.setText(detailText);

        TextView detailTextView2 = (TextView) inflatedView.findViewById(R.id.detailText2);
        detailTextView2.setText(detailText);

        TextView detailTextView3 = (TextView) inflatedView.findViewById(R.id.detailText3);
        detailTextView3.setText(detailText);

        return inflatedView;
    }
}

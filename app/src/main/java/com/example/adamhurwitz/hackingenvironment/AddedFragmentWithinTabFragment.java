package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddedFragmentWithinTabFragment extends Fragment {
    private final String LOG_TAG = LaunchNewActivityFragment.class.getSimpleName();

    //public static final String EXTRA_MESSAGE = "com.example.adamhurwitz.hackingenvironment.MainActivity";

    public AddedFragmentWithinTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.add_fragment_here, container, false);

        Button addFragment = (Button) inflatedView.findViewById(R.id.add_fragment_btn);
        addFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment();
            }
        });
        return inflatedView;
    }

    public void addFragment() {
        AddedFragmentWithinTab fragment = new AddedFragmentWithinTab();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}



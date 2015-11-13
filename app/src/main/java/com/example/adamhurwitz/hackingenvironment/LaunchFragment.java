package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class LaunchFragment extends Fragment {
    private final String LOG_TAG = NewActivityFragment.class.getSimpleName();

    //public static final String EXTRA_MESSAGE = "com.example.adamhurwitz.hackingenvironment.MainActivity";

    public LaunchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.add_fragment_test, container, false);

        return inflatedView;
    }

}

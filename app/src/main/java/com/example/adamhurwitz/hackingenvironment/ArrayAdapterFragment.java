package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArrayAdapterFragment extends Fragment {
    private final String LOG_TAG = ArrayAdapterFragment.class.getSimpleName();

    public ArrayAdapterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.arrayadapter_test, container, false);

        return inflatedView;
    }


}

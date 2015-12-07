package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

    /**
     * A placeholder fragment containing a simple view.
     */
    public class AsyncTaskFragment2 extends Fragment {
        private final String LOG_TAG = NewTestFragment.class.getSimpleName();

        public AsyncTaskFragment2() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View inflatedView = inflater.inflate(R.layout.new_test, container, false);

            return inflatedView;
        }
}

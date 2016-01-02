package com.example.adamhurwitz.hackingenvironment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by adamhurwitz on 1/2/16.
 */
public class StaticRecyclerViewFragment extends Fragment {

    private final String LOG_TAG = StaticRecyclerViewFragment.class.getSimpleName();

    public StaticRecyclerViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.staticrecyclerview_layout, container, false);

        setHasOptionsMenu(true);

        return inflatedView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_toast:
                Toast toast = Toast.makeText(getActivity(), "MENU BUTTON!", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

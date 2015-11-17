package com.example.adamhurwitz.hackingenvironment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArrayAdapterFragment extends Fragment {
    private final String LOG_TAG = ArrayAdapterFragment.class.getSimpleName();

    // Array of Images From Drawable Directory - - - - - - - - - - - - - - - - - - - - - - - - - - -
    private final Integer[] dummyData = {R.drawable.popular1,R.drawable.popular2,R.drawable.popular3,
            R.drawable.popular4,R.drawable.popular5,R.drawable.popular6,R.drawable.popular7,
            R.drawable.popular7,R.drawable.popular8,R.drawable.popular9,R.drawable.popular10,
            R.drawable.popular11,R.drawable.popular12,R.drawable.popular13,R.drawable.popular14,
            R.drawable.popular15,R.drawable.popular16,R.drawable.popular17,R.drawable.popular18};

    public ArrayAdapterFragment() {
    }
    //----------------------------------------------------------------------------------------------


   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.arrayadapter_test, container, false);

        return inflatedView;
    }*/


    // GridViewAdapter - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    // used for Array of Integers when using dummy data
    private class GridViewAdapter extends ArrayAdapter<Integer>{
        private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
        // declare Context variable
        Context context;

        /**
         * @param context  is the Context
         * @param resource is the grid_view_layout
         */
        // creates contructor to create GridViewAdapter object
        public GridViewAdapter(Context context, int resource) {

            super(context, resource, dummyData);
            this.context = context;
        }

        // getView to create view, telling Adapter what's included in the grid_item_layout
        @Override
        public View getView(int position, View view, ViewGroup parent) {

            // new method to only use memory when view is being used
            // layout inflater
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            // holder will hold the references to your views
            ViewHolder holder;

            // first clutter of views when nothing is loaded
            if (view == null) {
                // need inflator to inflate the grid_item_layout
                view = inflater.inflate(R.layout.grid_item_layout, parent, false);
                holder = new ViewHolder();
                // once view is inflated we can grab elements, getting and saving grid_item_imageview
                // as ImageView
                holder.gridItem = (ImageView) view.findViewById(R.id.grid_item_imageview);
                view.setTag(holder);
                // if view is not empty, re-use view to repopulate w/ data
            } else {
                holder = (ViewHolder) view.getTag();
            }

            // use setter method setImageResource() to set ImageView image from dummyData Array
            holder.gridItem.setImageResource(dummyData[position]);

            return view;
        }

        class ViewHolder {
            // declare your views here
            ImageView gridItem;
        }
    }

    // Create ArrayAdapter
    private ArrayAdapter dummyDataAdapter;

    // ArrayAdapter
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_view_layout, container, false);

        dummyDataAdapter = new GridViewAdapter(
                // current context (this fragment's containing activity)
                getActivity(),
                // ID of view item layout, not needed since we get it in getView()
                R.layout.grid_item_layout);


        // Get a reference to GridView, and attach this adapter to it
        GridView gridView = (GridView) view.findViewById(R.id.grid_view_layout);
        gridView.setAdapter(dummyDataAdapter);
        return view;
    }
    //----------------------------------------------------------------------------------------------

}

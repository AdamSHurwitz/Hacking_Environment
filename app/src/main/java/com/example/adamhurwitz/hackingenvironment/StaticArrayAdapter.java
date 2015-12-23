package com.example.adamhurwitz.hackingenvironment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class StaticArrayAdapter extends ArrayAdapter<Integer> {
    private final String LOG_TAG = StaticArrayAdapter.class.getSimpleName();

    // declare Context variable
    Context context;
    Integer[] dummyData;

    /**
     * @param context  is the Context
     * @param resource is the static_view_layout
     */
    // creates constructor to create StaticArrayAdapter object
    public StaticArrayAdapter(Context context, int resource, Integer[] dummyData) {

        super(context, resource, dummyData);
        this.context = context;
        this.dummyData = dummyData;
    }

    // getView to create view, telling Adapter what's included in the static_item_layout
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // new method to only use memory when view is being used
        // layout inflater
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        // holder will hold the references to your views
        ViewHolder holder;

        // first clutter of views when nothing is loaded
        if (view == null) {
            // need inflator to inflate the static_item_layout
            view = inflater.inflate(R.layout.static_item_layout, parent, false);
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

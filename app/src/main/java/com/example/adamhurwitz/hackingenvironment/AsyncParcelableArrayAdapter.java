package com.example.adamhurwitz.hackingenvironment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AsyncParcelableArrayAdapter extends ArrayAdapter<DoodleData> {
    // declare Context variable
    Context context;
    ArrayList<DoodleData> doodleDataList;

    /**
     * Constructor for the GridViewAdapter object.
     * @param context The context in which this adapter is called.
     * @param resource The id of the resource layout to use.
     * @param doodleDataList An array list of DoodleData objects.
     */
    // creates constructor to create StaticArrayAdapter object
    public AsyncParcelableArrayAdapter(Context context, int resource, ArrayList<DoodleData> doodleDataList) {

        super(context, resource, doodleDataList);
        this.context = context;
        this.doodleDataList = doodleDataList;
    }

    // getView to create view, telling Adapter what's included in the static_item_layout
    @Override
    /**
     * Overriding the getView method so that the adapter can recycle views appropriately when using
     * a grid.
     * @param position An integer representing the location of the view to pull from the list.
     * @param view The view to inflate with the different layouts for the grid.
     * @param parent The parent element of the view.
     */
    public View getView(int position, View view, ViewGroup parent) {

        // new method to only use memory when view is being used
        // layout inflater
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        // holder will hold the references to your views
        ViewHolder holder;

        // first clutter of views when nothing is loaded
        if (view == null) {
            // need inflator to inflate the static_item_layout
            view = inflater.inflate(R.layout.grid_recentitem_layout, parent, false);
            holder = new ViewHolder();

            // once view is inflated we can grab elements, getting and saving grid_item_imageview
            // as ImageView
            holder.gridItem = (ImageView) view.findViewById(R.id.grid_recentitem_imageview);
            view.setTag(holder);
            // if view is not empty, re-use view to repopulate w/ data
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context).load(doodleDataList.get(position).getImageUrl()).noFade()
                .into(holder.gridItem);

        return view;
    }

    class ViewHolder {
        // declare your views here
        ImageView gridItem;
    }
}

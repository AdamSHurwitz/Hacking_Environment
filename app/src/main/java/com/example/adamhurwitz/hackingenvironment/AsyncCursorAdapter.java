package com.example.adamhurwitz.hackingenvironment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.adamhurwitz.hackingenvironment.data.CursorContract;
import com.squareup.picasso.Picasso;


public class AsyncCursorAdapter extends android.widget.CursorAdapter {
    // declare Context variable
    Context context;

    /**
     * Constructor for the GridViewAdapter object.
     *
     * @param context The context in which this adapter is called.
     * @param cursor  Cursor from which to get the data
     * @param flags Determine behavior of adapter
     */
    // creates constructor to create StaticArrayAdapter object
    public AsyncCursorAdapter(Context context, Cursor cursor, int flags) {

        super(context, cursor, flags);
        this.context = context;
    }

    // getView to create view, telling Adapter what's included in the static_item_layout
    @Override
    /**
     * Overriding the getView method so that the adapter can recycle views appropriately when using
     * a grid.
     * @param context Context which points to activity
     * @param cursor Position auto-incremented
     * @param parent The parent element of the view.
     */
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // New method to only use memory when view is being used
        // layout inflater
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        // Holder will hold the references to your views
        ViewHolder holder;

        // Inflating new view for our layout
        View view = inflater.inflate(R.layout.grid_recentitem_layout, parent, false);
        holder = new ViewHolder();

        // Once view is inflated we can grab elements, getting and saving grid_item_imageview
        // as ImageView
        holder.gridItem = (ImageView) view.findViewById(R.id.grid_recentitem_imageview);
        view.setTag(holder);

        return view;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        String imageURL = cursor.getString(
                cursor.getColumnIndexOrThrow(CursorContract.ProductData.COLUMN_NAME_IMAGEURL));

        // Holder for a view
        ViewHolder holder = (ViewHolder) view.getTag();
        Picasso.with(context).load(imageURL).noFade()
                .into(holder.gridItem);
    }

    class ViewHolder {
        // declare your views here
        ImageView gridItem;
    }
}

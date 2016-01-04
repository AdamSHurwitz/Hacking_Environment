package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by adamhurwitz on 1/3/16.
 */
public class StaticRecyclerViewAdapter extends RecyclerView.Adapter<StaticRecyclerViewAdapter.ViewHolder> {

    // build ViewHolder subclass to create objects for data being passed into the Adapter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public final ImageView imageView;
        public final ImageView imageItem;
        public final TextView textItem;
        public final View view;

        public ViewHolder(View view) {
            super(view);
            imageItem = (ImageView) view.findViewById(R.id.recycler_item_image);
            textItem = (TextView) view.findViewById(R.id.recycler_item_text);
            this.view = view;
        }
    }

    // Initiate the following
    Context context;
    Integer[] imageData;
    String[] stringData;

    /**
     * Create Object for Adapter
     * @param context    is the Context
     * @param imageData  is Array of images
     * @param stringData is Array of image titles
     */
    // creates constructor to create StaticArrayAdapter object
    public StaticRecyclerViewAdapter(Context context, Integer[] imageData, String[] stringData) {
        //super(context, dummyData);
        this.context = context;
        this.imageData = imageData;
        this.stringData = stringData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staticrecycler_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       // gets position in static Array of images and sets it to ID defined in layout
        Glide.with(holder.imageItem.getContext())
                .load(imageData[position])
                .fitCenter()
                .into(holder.imageItem);
        // gets position in static Array of text and sets it to ID defined in layout
        holder.textItem.setText(stringData[position]);

        // click listener for detail view
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, StaticRecyclerViewDetailActivity.class);
                intent.putExtra("StaticRecylerViewExtra",
                        context.getString(R.string.detail_text));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageData.length;
    }
}

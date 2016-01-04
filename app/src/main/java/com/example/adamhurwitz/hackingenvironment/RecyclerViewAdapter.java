package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
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
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    Integer[] imageData;
    String[] stringData;

    /**
     * @param context  is the Context
     */
    // creates constructor to create StaticArrayAdapter object
    public RecyclerViewAdapter(Context context, Integer[] imageData, String[] stringData) {
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
        Glide.with(holder.imageItem.getContext())
                .load(imageData[position])
                .fitCenter()
                .into(holder.imageItem);
        holder.textItem.setText(stringData[position]);
    }

    @Override
    public int getItemCount() {
        return imageData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public final ImageView imageView;
        public final ImageView imageItem;
        public final TextView textItem;

        public ViewHolder(View view) {
            super(view);
            imageItem = (ImageView) view.findViewById(R.id.recycler_item_image);
            textItem = (TextView) view.findViewById(R.id.recycler_item_text);
        }
    }
}

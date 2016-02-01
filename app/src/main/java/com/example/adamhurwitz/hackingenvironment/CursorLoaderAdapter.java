package com.example.adamhurwitz.hackingenvironment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.adamhurwitz.hackingenvironment.data.ContentProviderContract;
import com.example.adamhurwitz.hackingenvironment.data.CursorContract;
import com.squareup.picasso.Picasso;


public class CursorLoaderAdapter extends android.widget.CursorAdapter {
    // declare Context variable
    Context context;
    String favorite;
    String doodleTitle;
    //Cursor mCursor;

    /**
     * Constructor for the GridViewAdapter object.
     *
     * @param context The context in which this adapter is called.
     * @param cursor  Cursor from which to get the data
     * @param flags   Determine behavior of adapter
     */
    // creates constructor to create StaticArrayAdapter object
    public CursorLoaderAdapter(Context context, Cursor cursor, int flags) {

        super(context, cursor, flags);
        this.context = context;
    }

    class ViewHolder {
        // declare your views here
        ImageView gridItem;
        ImageButton favoriteGridBtn;
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
        View view = inflater.inflate(R.layout.grid_loaderitem_layout, parent, false);
        holder = new ViewHolder();
        // Once view is inflated we can grab elements, getting and saving grid_item_imageview
        // as ImageView
        holder.gridItem = (ImageView) view.findViewById(R.id.grid_loaderitem_imageview);
        view.setTag(holder);
        holder.favoriteGridBtn = (ImageButton) view.findViewById(R.id.loadergridItem_favorite_button);

        return view;
    }

    public void bindView(View view, final Context context, Cursor cursor) {
        final Cursor mCursor = cursor;
        // Access database
        //CursorDbHelper mDbHelper = new CursorDbHelper(context);
        // Gets the data repository in read mode
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String imageURL = cursor.getString(
                cursor.getColumnIndexOrThrow(ContentProviderContract.ContentProviderProductData
                        .COLUMN_NAME_IMAGEURL));
        // Holder for a view
        final ViewHolder holder = (ViewHolder) view.getTag();
        Picasso.with(context).load(imageURL).noFade().into(holder.gridItem);

        favorite = cursor.getString(cursor.getColumnIndexOrThrow(ContentProviderContract
                .ContentProviderProductData.COLUMN_NAME_FAVORITE));
        doodleTitle = cursor.getString(cursor.getColumnIndexOrThrow(ContentProviderContract
                .ContentProviderProductData.COLUMN_NAME_TITLE));
        if (favorite.equals("1")) {
            holder.favoriteGridBtn.setImageResource(R.drawable.star_default_18dp);
        } else {
            holder.favoriteGridBtn.setImageResource(R.drawable.star_pressed_18dp);
        }

        //TODO: Figure out how to make Fav Btn functional onClick()
        // Create click listener
        holder.favoriteGridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                int rowsUpdated = 0;
                if (favorite.equals("1")) {
                    holder.favoriteGridBtn.setImageResource(R.drawable.star_pressed_18dp);
                    values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 2);
                } else {
                    holder.favoriteGridBtn.setImageResource(R.drawable.star_default_18dp);
                    values.put(CursorContract.ProductData.COLUMN_NAME_FAVORITE, 1);
                }
                rowsUpdated = context.getContentResolver().update(
                        ContentProviderContract.ContentProviderProductData.CONTENT_URI,
                        values, ContentProviderContract.ContentProviderProductData
                                .COLUMN_NAME_TITLE + "= ?", new String[]{doodleTitle});
            }
        });
    }
}

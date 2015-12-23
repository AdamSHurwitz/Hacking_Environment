package com.example.adamhurwitz.hackingenvironment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class SQLiteArrayAdapter extends ArrayAdapter<RowObject> {

    // Create ArrayList<> rowObjects
    private ArrayList<RowObject> rowObjects;

    // Create SQLiteArrayAdapter
    public SQLiteArrayAdapter(Context context, int resource, ArrayList<RowObject> rowObjects) {
        super(context, resource, rowObjects);
        this.rowObjects = rowObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Inflate table_row_view.xml
        View rowView = inflater.inflate(R.layout.table_row_view, parent, false);


        // get IDs of row layout inserting data into
        TextView entryID = (TextView) rowView.findViewById(R.id.entry_id);
        entryID.setText(String.valueOf(rowObjects.get(position).getID()));

        TextView concept = (TextView) rowView.findViewById(R.id.column_name_conecpt);
        concept.setText(String.valueOf(rowObjects.get(position).getConceptName()));

        TextView tabNum = (TextView) rowView.findViewById(R.id.column_name_tabnumber);
        tabNum.setText(String.valueOf(rowObjects.get(position).getTabNum()));

        return rowView;
    }
}
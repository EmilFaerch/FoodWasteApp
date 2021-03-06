package com.example.emilfrch.foodwaste;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Emil Færch on 14-04-2017.
 */

public class CustomAdapter extends ArrayAdapter<Item> {

    Context context;
    ArrayList<String> items;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtInfo;
    }

    public CustomAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.row_item);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder = new ViewHolder(); // view lookup cache stored in tag

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_item, null, true);

        viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
        viewHolder.txtName.setText("Title");

        viewHolder.txtInfo = (TextView) view.findViewById(R.id.txtInfo);
        viewHolder.txtInfo.setText("Info");

            convertView.setTag(viewHolder);
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.txtName.setText(item.getName());
        viewHolder.txtInfo.setText(item.getValue());
        // Return the completed view to render on screen
        return view;
    }
}
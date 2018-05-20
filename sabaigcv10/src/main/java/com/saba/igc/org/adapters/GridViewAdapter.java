package com.saba.igc.org.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saba.igc.org.R;

/**
 * Created by snaqvi on 6/13/16.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private final String[] string;
    private final int[] Imageid;

    public GridViewAdapter(Context c,String[] string,int[] Imageid ) {
        mContext = c;
        this.Imageid = Imageid;
        this.string = string;
    }

    @Override
    public int getCount() {
        return string.length;
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return 0;
    }

    @Override
    public View getView(int p, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.item_live_stream, null);
            TextView textView = (TextView) grid.findViewById(R.id.tvHallName);
            //ImageView imageView = (ImageView)grid.findViewById(R.id.gridview_image);
            textView.setText(string[p]);
            //imageView.setImageResource(Imageid[p]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
package com.saba.igc.org.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.models.LiveStreamFeed;

import java.util.ArrayList;

/**
 * Created by snaqvi on 6/26/16.
 */
public class LiveStreamFeedsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList mLiveFeedsArray = null;

    public LiveStreamFeedsAdapter(Context c, ArrayList array) {
        mContext = c;
        mLiveFeedsArray = array;
    }

    @Override
    public int getCount() {
        return mLiveFeedsArray.size();
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
        View item = null;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            item = new View(mContext);
            item = inflater.inflate(R.layout.item_live_stream, null);
            TextView textView = (TextView) item.findViewById(R.id.tvHallName);
            textView.setText( ((LiveStreamFeed)mLiveFeedsArray.get(p)).getHallName());

        } else {
            item = (View) convertView;
        }

        return item;
    }
}
package com.saba.igc.org.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.WeeklyProgramsArrayAdapter.ViewHolder;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.PrayTime;
import com.saba.igc.org.models.PrayerTimes;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class PrayTimeAdapter extends ArrayAdapter<PrayTime> {

	public PrayTimeAdapter(Context context,
			List<PrayTime> objects) {
		
		super(context, R.layout.pray_time_item, objects);
	}
	
	public static class ViewHolder{
		private TextView	mTvPrayerName;
		private TextView	mTvPrayerTime;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		// Get the data from position.
		final PrayTime prayTime = getItem(position);
		
		ViewHolder viewHolder =  null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.pray_time_item, parent, false);
			viewHolder.mTvPrayerName = (TextView)convertView.findViewById(R.id.tvPrayerName);
			viewHolder.mTvPrayerTime = (TextView)convertView.findViewById(R.id.tvPrayerTime);

			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}	
		
		updateVew(viewHolder, prayTime);
		return convertView;
	}
	
	private void updateVew(final ViewHolder viewHolder, PrayTime praytime){
		if(viewHolder == null || praytime == null){
			Log.e("error", "Invalid Arguments");
			return;
		}
		
		if(viewHolder.mTvPrayerName != null){
			viewHolder.mTvPrayerName.setText(praytime.getName());
		}
		
		if(viewHolder.mTvPrayerTime != null){
			viewHolder.mTvPrayerTime.setText(praytime.getTime());
		}
	}
}

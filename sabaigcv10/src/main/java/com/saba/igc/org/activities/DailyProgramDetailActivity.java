package com.saba.igc.org.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.WeeklyProgramsArrayAdapter;
import com.saba.igc.org.models.DailyProgram;

public class DailyProgramDetailActivity extends Activity {
	protected ListView mLvDailyPrograms;
	protected WeeklyProgramsArrayAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_programs_detail);
		mLvDailyPrograms = (ListView)findViewById(R.id.lvDailyPrograms);
		
		setupUI();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void setupUI() {
		String day = getIntent().getStringExtra("day");
		List<DailyProgram> dailyPrograms = null;
		if(day != null)
			dailyPrograms = DailyProgram.getPrograms(day);
		
		// removing program at 0 index which has the date info. Don't want to display here...
		if(dailyPrograms != null && dailyPrograms.size()>0)
			dailyPrograms.remove(0);
		
		String header = getIntent().getStringExtra("header");
		TextView tvHeader = (TextView)findViewById(R.id.tvHeader);
		if(tvHeader != null)
			tvHeader.setText(header);
		
		mAdapter = new WeeklyProgramsArrayAdapter(this, dailyPrograms);
		mLvDailyPrograms.setAdapter(mAdapter);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return true;
    }
}

package com.saba.igc.org.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.DailyProgramsArrayAdapter;
import com.saba.igc.org.models.DailyProgram;

import java.util.List;

public class DailyProgramDetailActivity extends AppCompatActivity {
	protected ListView 					mLvDailyPrograms;
	protected DailyProgramsArrayAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_programs_detail);
		mLvDailyPrograms = (ListView)findViewById(R.id.lvDailyPrograms);

		setupUI();

		// setting toolbar here...
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if(toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

			// sets toolbar title in center.
			View view = getLayoutInflater().inflate(R.layout.custom_toolbar_view, null);
			TextView tvTitle = (TextView)view.findViewById(R.id.tvToolbarTitle);
			String header = getIntent().getStringExtra("header");
			tvTitle.setText(header);
			toolbar.addView(view);
			setTitle(""); // to make API 16 compatible.
		}
	}

	private void setupUI() {
		String day = getIntent().getStringExtra("day");
		List<DailyProgram> dailyPrograms = null;
		if(day != null)
			dailyPrograms = DailyProgram.getPrograms(day);
		
		// removing program at 0 index which has the date info. Don't want to display here...
		if(dailyPrograms != null && dailyPrograms.size()>0)
			dailyPrograms.remove(0);
		
		mAdapter = new DailyProgramsArrayAdapter(this, dailyPrograms);
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

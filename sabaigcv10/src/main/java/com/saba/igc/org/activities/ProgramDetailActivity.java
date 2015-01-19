package com.saba.igc.org.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.WeeklyProgramsArrayAdapter;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

public class ProgramDetailActivity extends Activity {

	TextView mProgramTitle;
	TextView mProgramDetial;
	
	List<DailyProgram> mDailyPrograms;
	WeeklyProgramsArrayAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weekly_program_item);
		mProgramTitle = (TextView)findViewById(R.id.tvProgramTime);
		mProgramDetial = (TextView)findViewById(R.id.tvProgramDetail);
		
		SabaProgram program = (SabaProgram) getIntent().getParcelableExtra("program");
		mProgramTitle.setText(Html.fromHtml(program.getTitle()));
		//mProgramDetial.setText(program.getDescription());
		
		mProgramDetial.setText(Html.fromHtml(program.getDescription()));
		mProgramDetial.setMovementMethod(LinkMovementMethod.getInstance());
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

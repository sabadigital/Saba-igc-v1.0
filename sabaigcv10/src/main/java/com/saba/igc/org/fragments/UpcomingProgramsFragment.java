package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.saba.igc.org.R;
import com.saba.igc.org.models.SabaProgram;

/**
 * @author Syed Aftab Naqvi
 * @create December, 2014
 * @version 1.0
 */
public class UpcomingProgramsFragment extends SabaBaseFragment {
	private final String PROGRAM_NAME = "Upcoming Programs";
	private final String TAG = "UpcomingProgramsFragment";
	public UpcomingProgramsFragment(){
	}

	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		getActivity().setTitle(""); // Need this to make it little compatible with API 16. might work for API 14 as well.
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProgramName = PROGRAM_NAME;
		// get programs from database. if program exists then display. otherwise make a network request.
		mPrograms =  SabaProgram.getSabaPrograms(PROGRAM_NAME);
		if(mPrograms != null && mPrograms.size() == 0){
			mRefreshInProgress = true;
			// make a network request to pull the data from server.
			mSabaClient.getUpcomingPrograms(this);
		}

		// helps to display menu in fragments.
		setHasOptionsMenu(true);
	}
	
	@Override
	protected void populatePrograms() {
		mRefreshInProgress = true;
		mSwipeRefreshLayout.setRefreshing(true);
		mAdapter.clear();
		mSabaClient.getUpcomingPrograms(this);
	}
	
	@Override
	protected void processOnItemClick(int position){
	}

	//------ refresh menu item.
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.refresh_menu, menu);
	}

	// read about this exception....
//	java.lang.SecurityException: Permission Denial: get/set setting for user asks to run as user -2 but is calling from user 0; this requires android.permission.INTERACT_ACROSS_USERS_FULL



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refreshFragment:
				if(mRefreshInProgress)
					return true;

				populatePrograms();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}



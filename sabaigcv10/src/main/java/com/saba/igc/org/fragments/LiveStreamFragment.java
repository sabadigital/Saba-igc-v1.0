package com.saba.igc.org.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.saba.igc.org.R;
import com.saba.igc.org.activities.LiveMajlisActivity;
import com.saba.igc.org.adapters.LiveStreamFeedsAdapter;
import com.saba.igc.org.application.SabaClient;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.LiveStreamFeed;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by snaqvi on 6/25/16.
 */

public class LiveStreamFragment extends Fragment implements SabaServerResponseListener{

    private GridView mGvLiveStreamHalls     = null;
    private ArrayList mLiveStreamFeeds      = null;
    private LiveStreamFeedsAdapter mAdapter = null;
    private ProgressBar mProgressBar        = null;
    public LiveStreamFragment(){
        mLiveStreamFeeds = new ArrayList(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SabaClient.getInstance(getActivity()).getLiveStreamFeeds(this);

        View rootView = inflater.inflate(R.layout.fragment_live_stream, container, false);
        mGvLiveStreamHalls = (GridView) rootView.findViewById(R.id.gvLiveStreamHalls);
        mAdapter = new LiveStreamFeedsAdapter(getActivity(), mLiveStreamFeeds);
        mGvLiveStreamHalls.setAdapter(mAdapter);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.gvProgressBar);

        //OnItemClickListener
        mGvLiveStreamHalls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), LiveMajlisActivity.class);
                LiveStreamFeed liveStreamFeed = (LiveStreamFeed)mLiveStreamFeeds.get(position);
                String videoId = liveStreamFeed.getVideoId();
                String hallName = liveStreamFeed.getHallName();

                intent.putExtra("videoId", videoId);
                intent.putExtra("hallName", hallName);

                startActivity(intent);
                return;
            }
        });

        return rootView;
    }

    @Override
    public void processJsonObject(String programName, JSONObject response) {
        mLiveStreamFeeds.clear();
        mProgressBar.setVisibility(View.GONE);
        //TODO: extract error and send tracking event.
        if(response == null || response.length() == 0) {
            // Send a tracking event in case of failure.
            Log.e("LiveStreamFragment", "JSONObject is null or zero length");
            return;
        }

        try {
            JSONArray array = response.names();
            int feedCount = array.length();
            for(int i=0; i<feedCount; i++){
                LiveStreamFeed liveStreamData = new LiveStreamFeed();
                String hallName = array.getString(i);

                if(hallName != null) {
                    liveStreamData.setHallName(hallName);
                    JSONObject obj = response.getJSONObject(hallName);
                    if(obj != null) {
                        liveStreamData.setVideoId(obj.getString("path"));
                        mLiveStreamFeeds.add(liveStreamData);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @Override
    public void processJsonObject(String programName, JSONArray response) {
        //TODO: extract error and send tracking event.
        if(response == null || response.length() == 0) {
            // Send a tracking event in case of failure.
            Log.e("LiveStreamFragment", "JSONObject is null or zero length");
            return;
        }
    }
}

package com.saba.igc.org.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.saba.igc.org.R;
import com.saba.igc.org.extras.DeveloperKey;

/**
 * Created by snaqvi on 6/25/16.
 */

public class LiveMajlisFragment extends Fragment {

    private FragmentActivity myContext;
    private String mVideoId;
    private YouTubePlayer mYoutubePlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live_majlis, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, new OnInitializedListener() {
            @Override
            public void onInitializationSuccess(Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    mYoutubePlayer = youTubePlayer;
                    mYoutubePlayer.setFullscreen(true);
                    mYoutubePlayer.loadVideo(mVideoId);
                    mYoutubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub
                // Send Tracking event....
            }
        });

        return rootView;
    }

    public void setVideoId(String videoId){
        mVideoId = videoId;
    }
}
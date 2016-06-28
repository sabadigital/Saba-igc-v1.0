package com.saba.igc.org.models;

/**
 * Created by snaqvi on 6/26/16.
 */
public class LiveStreamFeed{
    private String mVideoId;
    private String mHallName;

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        this.mVideoId = videoId;
    }

    public String getHallName() {
        return mHallName;
    }

    public void setHallName(String hallName) {
        this.mHallName = hallName;
    }
}
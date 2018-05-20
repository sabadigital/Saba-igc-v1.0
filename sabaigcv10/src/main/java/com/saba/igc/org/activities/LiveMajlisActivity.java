package com.saba.igc.org.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.saba.igc.org.R;
import com.saba.igc.org.fragments.LiveMajlisFragment;

/**
 * Created by snaqvi on 6/25/16.
 */

/*
* LiveMajlisActivity works in a different way. This activity is inherited from AppCompatActivity so we can get
* the title bar. Also, we added toolbar into it and shows title as well.
*
* We shows fragment into this activity, that fragment hosts the youtube fragment which shows the majlis.
*
*
*
* */
public class LiveMajlisActivity extends AppCompatActivity {
    private Toolbar             mToolbar;
    private TextView 	        mTvToolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_live_majlis);

        // setting toolbar here...
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setBackgroundColor(Color.BLACK);

            // sets toolbar title in center.
            View view = getLayoutInflater().inflate(R.layout.custom_toolbar_view, null);
            mTvToolbarTitle = (TextView)view.findViewById(R.id.tvToolbarTitle);
            mToolbar.addView(view);
        }

        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");
        String hallName = intent.getStringExtra("hallName");
        LiveMajlisFragment  liveMajlisFragment = null;
        try {
            liveMajlisFragment = LiveMajlisFragment.class.newInstance();
            liveMajlisFragment.setVideoId(videoId);
            mTvToolbarTitle.setText(hallName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment,
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_contents, liveMajlisFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
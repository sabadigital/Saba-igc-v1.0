package com.saba.igc.org.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.saba.igc.org.R;

/**
 * Created by Syed Aftab Naqvi on 1/18/15.
 */
public class SplashScreenActivity extends Activity{
    private TextView mTvContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setupUI();
    }

    private void setupUI() {
        mTvContinue = (TextView)findViewById(R.id.tvContinue);
        if(mTvContinue != null) {
            mTvContinue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity1.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}

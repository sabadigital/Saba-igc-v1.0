package com.saba.igc.org.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.saba.igc.org.R;
import com.saba.igc.org.application.SabaApplication;
import com.saba.igc.org.listeners.SabaServerResponseListener;
import com.saba.igc.org.models.DailyProgram;
import com.saba.igc.org.models.SabaProgram;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Syed Aftab Naqvi on 1/18/15.
 */

public class SplashScreenActivity extends Activity implements SabaServerResponseListener {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        // on each fresh launch, try to pull the fresh data from server.
        SabaApplication.getSabaClient().getWeeklyPrograms(this);
//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Intent i = new Intent(SplashScreenActivity.this, MainActivity1.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }

    public void processJsonObject(String programName, JSONObject response) {
        // we received an error... may be internet coonection not found. Showing empty activity.
        Intent i = new Intent(SplashScreenActivity.this, MainActivity1.class);
        startActivity(i);

        // close this activity
        finish();
    }

    public void processJsonObject(String programName, JSONArray response){
        if(response != null) {
            List<SabaProgram> programs = null;
            if (programName != null && programName.compareToIgnoreCase("Weekly Programs") == 0) {
                // parse weekly programs differently....
                List<List<DailyProgram>> weeklyPrograms = DailyProgram.fromJSONArray(programName, response);
                programs = SabaProgram.fromWeeklyPrograms(programName, weeklyPrograms);

                addAll(programs);
                addAllWeeklyPrograms(weeklyPrograms);
            }
        }

        // our database is ready with if there is no error, lets start the new activity.
        Intent i = new Intent(SplashScreenActivity.this, MainActivity1.class);
        startActivity(i);

        // close this activity
        finish();
    }

    // Delegate the adding to the internal adapter. // most recommended approach... minimize the code...
    private void addAll(List<SabaProgram> programs){
        // delete existing records if there is any. We don't want to keep duplicate entries.
        SabaProgram.deleteSabaPrograms("Weekly Programs");

        // save new/latest programs.
        for(final SabaProgram program : programs){
            program.saveProgram();
        }
    }

    // delete old data from the WeeklyProgram table and then save all newly retrieved weekly Programs.
    private void addAllWeeklyPrograms(List<List<DailyProgram>> programs){
        // delete existing records. We don't want to keep duplicate/old entries.
        DailyProgram.deletePrograms();

        // save new/latest programs.
        for(final List<DailyProgram> dailyPrograms : programs){
            for(final DailyProgram program : dailyPrograms){
                program.saveProgram();
            }
        }
    }

}
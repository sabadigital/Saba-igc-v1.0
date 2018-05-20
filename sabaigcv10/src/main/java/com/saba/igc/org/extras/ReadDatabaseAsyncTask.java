package com.saba.igc.org.extras;

import android.os.AsyncTask;

import com.saba.igc.org.listeners.AsyncTaskListener;
import com.saba.igc.org.models.SabaProgram;

import java.util.List;

/**
 * Created by snaqvi on 6/19/15.
 */

// good article to complete this Task
// http://mrjoelkemp.com/2011/11/android-handler-based-communication/
// Also look for BetterASyncTask at https://searchcode.com/codesearch/view/25317137/.
// /src/com/codebutler/farebot/BetterAsyncTask.java

// I will finish and implement this task in future when time will permit.
public class ReadDatabaseAsyncTask extends AsyncTask<String, Void, List<SabaProgram>> {
    private String mProgramName;
    private AsyncTaskListener mAsyncTaskListener;

    public ReadDatabaseAsyncTask(AsyncTaskListener asyncTaskListener){
        mAsyncTaskListener = asyncTaskListener;
    }

    @Override
    protected List<SabaProgram> doInBackground(String... programName) {
        mProgramName = programName[0];
        return SabaProgram.getSabaPrograms(programName[0]);
    }

    @Override
    protected void onPostExecute(List<SabaProgram> result) {
        super.onPostExecute(result);

        if(result != null){
            System.out.println(result.get(0).getTitle());
        }
        mAsyncTaskListener.onAsyncTaskFinished(result); // make sure if we need pass back the programName.
    }
}

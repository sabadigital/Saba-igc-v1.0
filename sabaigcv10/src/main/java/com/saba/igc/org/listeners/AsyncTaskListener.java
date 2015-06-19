package com.saba.igc.org.listeners;

import com.saba.igc.org.models.SabaProgram;

import java.util.List;

/**
 * Created by snaqvi on 6/19/15.
 */
public interface AsyncTaskListener {
    public abstract void onAsyncTaskFinished(List<SabaProgram> programs);
}

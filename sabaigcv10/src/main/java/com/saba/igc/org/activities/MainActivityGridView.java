package com.saba.igc.org.activities;

/**
 * Created by snaqvi on 6/13/16.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.saba.igc.org.R;
import com.saba.igc.org.adapters.GridViewAdapter;

import java.util.ArrayList;

public class MainActivityGridView extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    GridView gridView;
    Context context;
    ArrayList arrayList;

    public static String[] gridViewStrings = {
            "Android",
            "Java",
            "GridView",
            "ListView",

    };
    public static int[] gridViewImages = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_gridview);

        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new GridViewAdapter(this, gridViewStrings, gridViewImages));

        //initInstances();
    }

    private void initInstances() {

    }

}

package com.saba.igc.org.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by snaqvi on 6/9/15.
 */

@Table(name="HijriDate")
public class HijriDate {

    @Column(name = "englishDate")
    private String mEnglishDate;

    @Column(name = "hijriDate")
    private String mHijriDate;
}

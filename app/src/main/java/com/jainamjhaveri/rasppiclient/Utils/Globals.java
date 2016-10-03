package com.jainamjhaveri.rasppiclient.Utils;

import android.graphics.Color;

import java.text.DateFormat;
import java.util.Date;

public class Globals {

    public static final int circleColor = Color.DKGRAY;
    public static final int fillColor = Color.argb(111, 140, 234, 255);
    public static final int axisLineColor = Color.RED;
    public static final int lookaheads = 10;
    public static final String subscribeKey = "sub-c-35707b44-653e-11e6-9c81-02ee2ddab7fe";
    public static int xindex = 0;
    public static final long ref = System.currentTimeMillis();
    public static final String TITLE_GRAPH = "Graph View";
    public static final String TITLE_TABLE = "Table View";

    public static final char IS_TABLE_FRAGMENT = 't';
    public static final char IS_GRAPH_FRAGMENT = 'g';
    public static char currentFragment;

    private static final DateFormat mDataFormat = DateFormat.getDateTimeInstance();
    private static Date mTime = new Date();

    public static String getTimeString(long timestamp)
    {
        mTime.setTime( timestamp );
        return mDataFormat.format( mTime );
    }


}
// TODO: Save Data on Daily Basis and load/view previous data. Storing on local storage
// TODO: Remove hourly/daily data
// TODO: Sortable Tableview



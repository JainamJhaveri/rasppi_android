package com.jainamjhaveri.rasppiclient;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DateFormat;
import java.util.Date;

public class HourAxisValueFormatter implements AxisValueFormatter
{

    private final String TAG = this.getClass().getSimpleName();
    private DateFormat mDataFormat;
    private Date mTime;

    public HourAxisValueFormatter()
    {
        this.mDataFormat = DateFormat.getTimeInstance();
        this.mTime = new Date();
    }


    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */
    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
//        Log.e(TAG, "getFormattedValue:: value: "+value );
        long originalTimestamp = Globals.ref + (long)value * 1000;
        mTime.setTime( originalTimestamp );
        return mDataFormat.format( mTime );
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }


}
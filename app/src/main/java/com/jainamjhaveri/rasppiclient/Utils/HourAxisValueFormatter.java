package com.jainamjhaveri.rasppiclient.Utils;

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


    @Override
    public String getFormattedValue(float value, AxisBase axis)
    {
        long originalTimestamp = Globals.ref + (long)value * 1000;
        mTime.setTime( originalTimestamp );
        return mDataFormat.format( mTime );
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }


}
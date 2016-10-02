package com.jainamjhaveri.rasppiclient;


import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DateFormat;
import java.util.Date;

import static com.jainamjhaveri.rasppiclient.Globals.ref;

public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private DateFormat mDataFormat;
    private Date mTime;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.mDataFormat = DateFormat.getDateTimeInstance();
        this.mTime = new Date();
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long timestamp = ref + (long)e.getX() * 1000;
        mTime.setTime( timestamp );
        tvContent.setText(e.getY() + " at " + mDataFormat.format( mTime ) ); // set the entry-value as the display text
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
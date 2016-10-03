package com.jainamjhaveri.rasppiclient.Graph;


import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.jainamjhaveri.rasppiclient.R;

import static com.jainamjhaveri.rasppiclient.Utils.Globals.getTimeString;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.ref;

public class MyMarkerView extends MarkerView {

    private TextView tvContent;


    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long timestamp = ref + (long)e.getX() * 1000;
        tvContent.setText(e.getY() + " at " + getTimeString( timestamp ) ); // set the entry-value as the display text
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
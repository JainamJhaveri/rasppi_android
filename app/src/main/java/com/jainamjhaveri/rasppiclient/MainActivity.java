package com.jainamjhaveri.rasppiclient;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
//    private int greenColor = Color.rgb(104, 241, 175);
    private int circleColor = Color.DKGRAY;
    private int fillColor = Color.argb( 111 , 140 , 234 , 255);
    private int axisLineColor = Color.RED;

    long ref = System.currentTimeMillis();
    private LineChart mChart;
    private PNConfiguration pnConfiguration;
    private PubNub pubnub;
    private int lookaheads = 8;
    private String subscribeKey = "sub-c-35707b44-653e-11e6-9c81-02ee2ddab7fe";
    MyMarkerView myMarkerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.my_marker_view_layout, ref);
        initPubNubConfig();
        initializeChart();
    }

    private void initPubNubConfig()
    {
        pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("");
        pnConfiguration.setSubscribeKey(subscribeKey);
        pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback()
        {
            @Override
            public void status(PubNub pubnub, PNStatus status)
            {

                if(status.getOperation() == PNOperationType.PNSubscribeOperation)
                {
                    Log.e(TAG, "status: subscribe operation" );
                    if(status.getCategory() == PNStatusCategory.PNConnectedCategory)
                    {
                        Log.e(TAG, "status: connected !" );
                    }
                }
            }

            public void message(PubNub pubnub, PNMessageResult message)
            {

                float recievedPoint = (float) message.getMessage().asDouble();

                Log.e( TAG, "message: "+ recievedPoint );

                // TODO: get time somehow and not from timetoken
                addEntry( recievedPoint, System.currentTimeMillis() );

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {}

        });
        pubnub.subscribe().channels(Arrays.asList("my_channel")).execute();
    }

    static int index = 0;
    private void addEntry(float point, long timestamp)
    {

        LineData data = mChart.getData();
        ILineDataSet set;

        if (data.getDataSetByIndex(0) == null)
        {
            set = createSet();
            data.addDataSet(set);
        }

        data.addEntry( new Entry(index++, point), 0 );

        data.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(lookaheads);

        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);

    }


    private LineDataSet createSet()
    {

        LineDataSet set = new LineDataSet(null, "Sensor Distance from RaspPi");

        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setDrawCircles(true);
        set.setLineWidth(1.8f);
        set.setCircleRadius(4f);
        set.setCircleColor(circleColor);

        set.setDrawFilled(true);
        set.setFillColor(fillColor);
        set.setDrawHorizontalHighlightIndicator(true);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }

    private void initializeChart()
    {

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setData(new LineData());
        assert mChart != null;
        mChart.setBackgroundColor(Color.WHITE);

        // enable specific touch gestures & disable description
        mChart.setTouchEnabled(true);
        mChart.setHorizontalScrollBarEnabled(true);
        mChart.setVerticalScrollBarEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setDescription("");


        XAxis x = mChart.getXAxis();
        YAxis y = mChart.getAxisLeft();

        x.setAxisMinValue(0);
        y.setAxisMinValue(0);

        x.setLabelCount(lookaheads, false);
        y.setLabelCount(6, false);

        y.setTextColor(Color.BLACK);
        y.setTypeface(Typeface.DEFAULT_BOLD);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        x.setTextColor(Color.BLACK);
        x.setTypeface(Typeface.DEFAULT_BOLD);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        x.setAxisLineColor(axisLineColor);
        y.setAxisLineColor(axisLineColor);

        x.setDrawGridLines(false);
        y.setDrawGridLines(true);


        x.setEnabled(true);
        y.setEnabled(true);

        x.setLabelRotationAngle(270);

        AxisValueFormatter xAxisFormatter = new HourAxisValueFormatter( ref );
        x.setValueFormatter(xAxisFormatter);

        x.setAvoidFirstLastClipping(true);
        mChart.getAxisRight().setEnabled(false);


        mChart.setMarkerView(myMarkerView);
        mChart.animateXY(1000, 1000);


        // dont forget to refresh the drawing
        mChart.invalidate();

    }

}
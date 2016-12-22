package com.jainamjhaveri.rasppiclient.Graph;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;
import com.jainamjhaveri.rasppiclient.Utils.HourAxisValueFormatter;

import java.util.ArrayList;

import static com.jainamjhaveri.rasppiclient.MainActivity.getArrayList;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.axisLineColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.circleColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.fillColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.lookaheads;

public class GraphFragment extends Fragment {

    private static GraphFragment mGraphFragment;
    private final String TAG = this.getClass().getSimpleName();
    private LineChart mChart;
    private MyMarkerView myMarkerView;
    private LineData mLineData;
    public GraphFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGraphFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mChart = (LineChart) view.findViewById(R.id.chart);
        myMarkerView = new MyMarkerView( this.getContext(), R.layout.my_marker_view_layout );
        
        mLineData = new LineData();
        initializeChart();

        return view;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(getArrayList() == null) return;
        Log.e(TAG, "onViewStateRestored graphfragment: " + getArrayList().size());
        if( getArrayList().size() > 0 )
            recreateChart();
    }

    private void recreateChart()
    {
        LineData data = mLineData;
        Log.e(TAG, "recreateChart: dataEntryCount: "+ data.getEntryCount() );
        if(data.getEntryCount() > 0)
        {
            mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);
            return;
        }
        if (data.getDataSetByIndex(0) == null)
        {
            data.addDataSet( createSet() );
        }

        ArrayList<DataPoint> arrayList = getArrayList();
        if(arrayList == null) return;

        for( int i=0; i<arrayList.size(); i++)
        {
            data.addEntry(new Entry(i, arrayList.get(i).getPoint()), 0);
        }

        data.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(lookaheads);

        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);
    }

    private static LineDataSet createSet() {

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

    private void initializeChart() {

        mChart.setData(mLineData);
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

        AxisValueFormatter xAxisFormatter = new HourAxisValueFormatter();
        x.setValueFormatter(xAxisFormatter);

//        x.setAvoidFirstLastClipping(true);
        mChart.getAxisRight().setEnabled(false);


        mChart.setMarkerView(myMarkerView);
//        mChart.animateXY(1000, 1000);


        // dont forget to refresh the drawing
        mChart.invalidate();

    }


    public void updateGraph()
    {

        clearGraph();


        LineData data = mLineData;
        data.clearValues();
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mChart.invalidate();
                    }
                }
        );

        if (data.getDataSetByIndex(0) == null)
        {
            System.out.println("DatasetbygetIndex(0) is null");
            data.addDataSet( createSet() );
        }

        ArrayList<DataPoint> arrayList = getArrayList();
//        System.out.println(arrayList);
        System.out.print(data.getEntryCount()+" ");
        if(arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                data.addEntry(new Entry(i, arrayList.get(i).getPoint()), 0);
            }
        }
        System.out.println(data.getEntryCount());
        data.notifyDataChanged();
        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mChart.notifyDataSetChanged();
                        mChart.setVisibleXRangeMaximum(lookaheads);
                    }
                }
        );


        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);
    }

    private void clearGraph() {
        mLineData.clearValues();
    }


    public static GraphFragment getInstance() {
        return mGraphFragment;
    }
}
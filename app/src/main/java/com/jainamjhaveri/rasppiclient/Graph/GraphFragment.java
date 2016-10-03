package com.jainamjhaveri.rasppiclient.Graph;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.jainamjhaveri.rasppiclient.MainActivity;
import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Table.TableFragment;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;
import com.jainamjhaveri.rasppiclient.Utils.HourAxisValueFormatter;

import java.util.ArrayList;

import static com.jainamjhaveri.rasppiclient.Utils.Globals.IS_GRAPH_FRAGMENT;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.TITLE_TABLE;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.axisLineColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.circleColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.currentFragment;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.fillColor;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.lookaheads;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.xindex;

public class GraphFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static LineChart mChart;
    private MyMarkerView myMarkerView;

    public GraphFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tablemenu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == R.id.menu_table )
        {
            Fragment fragment = new TableFragment();
            getActivity().setTitle(TITLE_TABLE);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        currentFragment = IS_GRAPH_FRAGMENT;

        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mChart = (LineChart) view.findViewById(R.id.chart);
        myMarkerView = new MyMarkerView( this.getContext(), R.layout.my_marker_view_layout );
        initializeChart();

        return view;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(TAG, "onViewStateRestored: " + MainActivity.getArrayList().size());
        if( MainActivity.getArrayList().size() > 0 )
            recreateChart();
    }

    private void recreateChart()
    {
        LineData data = mChart.getData();
        Log.e(TAG, "recreateChart: dataEntryCount: "+data.getEntryCount() );
        if(data.getEntryCount() > 0)
        {
            mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);
            return;
        }

        ArrayList<DataPoint> arrayList = MainActivity.getArrayList();
        if (data.getDataSetByIndex(0) == null)
        {
            data.addDataSet( createSet() );
        }

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

        AxisValueFormatter xAxisFormatter = new HourAxisValueFormatter();
        x.setValueFormatter(xAxisFormatter);

//        x.setAvoidFirstLastClipping(true);
        mChart.getAxisRight().setEnabled(false);


        mChart.setMarkerView(myMarkerView);
        mChart.animateXY(1000, 1000);


        // dont forget to refresh the drawing
        mChart.invalidate();

    }

    public static void updateGraph(DataPoint object)
    {

        LineData data = mChart.getData();
        if (data.getDataSetByIndex(0) == null)
        {
            data.addDataSet( createSet() );
        }

        data.addEntry(new Entry(xindex, object.getPoint()), 0);

        data.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(lookaheads);

        // this automatically refreshes the chart (calls invalidate())
        mChart.moveViewTo(data.getEntryCount() - (lookaheads + 1), 50f, YAxis.AxisDependency.LEFT);
    }
}

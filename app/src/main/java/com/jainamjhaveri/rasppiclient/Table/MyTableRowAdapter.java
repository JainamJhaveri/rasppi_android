package com.jainamjhaveri.rasppiclient.Table;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

import static com.jainamjhaveri.rasppiclient.Utils.Globals.getTimeString;

class MyTableRowAdapter extends TableDataAdapter<DataPoint> {

    private static final int TEXT_SIZE = 15;

    MyTableRowAdapter(Context context, List<DataPoint> data)
    {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        DataPoint dp = getRowData(rowIndex);
        View renderedView = null;

        switch(columnIndex)
        {
            case 0:
                renderedView = renderValue(dp);
                break;
            case 1:
                renderedView = renderTimeStamp(dp);
                break;
        }

        return renderedView;
    }

    private View renderValue(DataPoint dp) {
        final String point = String.valueOf(dp.getPoint());

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams( new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        final TextView textView = new TextView(getContext());
        textView.setText( point );
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        linearLayout.addView(textView, 0);

        return linearLayout;
    }


    private View renderTimeStamp(DataPoint dp) {
        final String point = getTimeString( dp.getTime()) ;

        final TextView textView = new TextView(getContext());
        textView.setText( point );
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        return textView;
    }
}

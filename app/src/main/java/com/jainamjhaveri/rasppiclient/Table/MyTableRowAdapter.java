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

public class MyTableRowAdapter extends TableDataAdapter<DataPoint> {

    private static final int TEXT_SIZE = 15;

    public MyTableRowAdapter(Context context, List<DataPoint> data)
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

//        linearLayout.setGravity(Gravity.RIGHT);
//        View v1 = new View(getContext());
//        v1.setLayoutParams(new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.MATCH_PARENT));
//        linearLayout.addView(v1, 1);
//        View v2 = new View(getContext());
//        v2.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
//        v2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
//        linearLayout.addView(v2, 2);

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

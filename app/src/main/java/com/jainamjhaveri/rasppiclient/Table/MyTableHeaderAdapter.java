package com.jainamjhaveri.rasppiclient.Table;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jainamjhaveri.rasppiclient.R;

import de.codecrafters.tableview.TableHeaderAdapter;

class MyTableHeaderAdapter extends TableHeaderAdapter {
    private static final int TEXT_SIZE = 18;

    MyTableHeaderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getHeaderView(int columnIndex, ViewGroup parentView) {
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderValueHeader("Value");
                break;
            case 1:
                renderedView = renderTimeHeader("Time");
                break;
        }

        return renderedView;
    }

    private View renderValueHeader(String valuehead) {
        final TextView textView = new TextView(getContext());

        textView.setText(valuehead);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private View renderTimeHeader(String timehead) {
        final TextView textView = new TextView(getContext());

        textView.setText(timehead);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
//        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

        return textView;
    }
}

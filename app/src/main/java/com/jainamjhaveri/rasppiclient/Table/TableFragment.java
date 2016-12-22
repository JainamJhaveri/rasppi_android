package com.jainamjhaveri.rasppiclient.Table;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import static com.jainamjhaveri.rasppiclient.MainActivity.getArrayList;


public class TableFragment extends Fragment {

    private static TableFragment mTableFragmentInstance;
    private final String TAG = this.getClass().getSimpleName();
    private TableView<DataPoint> tableView;
    private static TableDataAdapter<DataPoint> adapter;
    private static List<DataPoint> mList;

    public TableFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mTableFragmentInstance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        tableView = (TableView<DataPoint>) view.findViewById(R.id.tableView);
        tableView.setColumnWeight(0, 4);
        tableView.setColumnWeight(1, 6);

        initTableHeader();
        initTableRowBackground();

        mList = new ArrayList<>();
        adapter = new MyTableRowAdapter(this.getContext(), mList);

        return view;
    }

    private void initTableHeader() {
        tableView.setHeaderBackground(R.drawable.green_round);
        tableView.setHeaderAdapter(new MyTableHeaderAdapter(this.getContext()));
    }

    private void initTableRowBackground() {
        final int rowColorEven = ContextCompat.getColor(this.getContext(), R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(this.getContext(), R.color.table_data_row_odd);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(mList.size() != 0) return;
//        assert getArrayList()!=null;
//        Log.e(TAG, "onViewStateRestored tablefragment: " + mList.size() + " " + getArrayList().size());
        reCreateTable();


//        super.onViewStateRestored(savedInstanceState);
//        if(getArrayList() == null) return;
//        Log.e(TAG, "onViewStateRestored tablefragment: " + mList.size() + " " + getArrayList().size());
//        if (mList.size() == 0 && getArrayList().size() > 0)
//            reCreateTable();
//
    }

    /**
     * recreate table by adding the global ArrayList's content into this table's List<DataPoint>
     */
    private void reCreateTable() {
        if(getArrayList() == null) return;

        mList.addAll(getArrayList());
        Collections.reverse(mList);
        adapter.notifyDataSetChanged();
        tableView.setDataAdapter(adapter);
    }

    /**
     * clear the table's current data from List<DataPoint> which populates it
     * recreate table by adding the global ArrayList's content into this table's List<DataPoint>
     */
    public void updateTable() {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        clearTable();
                        reCreateTable();
                    }
                }
        );
    }

    private void clearTable() {
        mList.clear();
        adapter.notifyDataSetChanged();
        tableView.setDataAdapter(adapter);
    }

    public static TableFragment getInstance() {
        return mTableFragmentInstance;
    }

}
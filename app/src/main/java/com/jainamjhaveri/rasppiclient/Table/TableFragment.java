package com.jainamjhaveri.rasppiclient.Table;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jainamjhaveri.rasppiclient.MainActivity;
import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.graphmenu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.menu_graph) {
//            Fragment fragment = new GraphFragment();
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        Log.e(TAG, "onViewStateRestored tablefragment: " + mList.size() + " " + MainActivity.getArrayList().size());
        if (mList.size() == 0 && MainActivity.getArrayList().size() > 0)
            reCreateTable();
    }

    private void reCreateTable() {
        mList.addAll(MainActivity.getArrayList());
        Collections.reverse(mList);
        adapter.notifyDataSetChanged();
        tableView.setDataAdapter(adapter);
    }


    public void updateTable(final DataPoint object) {
        if (getActivity() == null) return;


        getActivity().runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        if (mList.size() == 0 && MainActivity.getArrayList().size() > 0) {
                            mList.addAll(MainActivity.getArrayList());
                            Collections.reverse(mList);
                        }
                        mList.add(0, object);
                        adapter.notifyDataSetChanged();
                        tableView.setDataAdapter(adapter);
                    }
                }
        );
    }

    public static TableFragment getInstance() {
        return mTableFragmentInstance;
    }

    public static void updateSensorData() {
        mTableFragmentInstance.reCreateTable();
    }
}
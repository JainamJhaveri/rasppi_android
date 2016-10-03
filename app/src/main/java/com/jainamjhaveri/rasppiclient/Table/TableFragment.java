package com.jainamjhaveri.rasppiclient.Table;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jainamjhaveri.rasppiclient.Graph.GraphFragment;
import com.jainamjhaveri.rasppiclient.MainActivity;
import com.jainamjhaveri.rasppiclient.R;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;

import java.util.ArrayList;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import static com.jainamjhaveri.rasppiclient.Utils.Globals.IS_TABLE_FRAGMENT;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.TITLE_GRAPH;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.currentFragment;


public class TableFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private static TableView<DataPoint> tableView;
    private static ArrayList<DataPoint> mList;

    public TableFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.graphmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_graph) {
            Fragment fragment = new GraphFragment();
            getActivity().setTitle(TITLE_GRAPH);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        currentFragment = IS_TABLE_FRAGMENT;

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
    static TableDataAdapter<DataPoint> adapter;
    private void initTableHeader() {
        tableView.setHeaderBackground(R.drawable.green_round);
        tableView.setHeaderAdapter(new MyTableHeaderAdapter(this.getContext()));
    }

    private void initTableRowBackground() {
        final int rowColorEven = ContextCompat.getColor(this.getContext(), R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(this.getContext(), R.color.table_data_row_odd);
        tableView.setDataRowBackgroundProvider( TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd) );
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(TAG, "onViewStateRestored: " + MainActivity.getArrayList().size());
        if( MainActivity.getArrayList().size() > 0 )
            reCreateTable();
    }

    private void reCreateTable()
    {
        ArrayList<DataPoint> arrayList = MainActivity.getArrayList();
        mList = null;
        mList = new ArrayList<>();
        for( DataPoint dp: arrayList )
        {
            mList.add(dp);
        }
        adapter.notifyDataSetChanged();
        tableView.setDataAdapter(adapter);
    }



    public static void updateTable(DataPoint object)
    {
        mList.add(object);
//        adapter.notifyDataSetChanged();
//        tableView.setDataAdapter(adapter);
    }
}
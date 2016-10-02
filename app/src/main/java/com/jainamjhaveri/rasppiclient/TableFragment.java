package com.jainamjhaveri.rasppiclient;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import static com.jainamjhaveri.rasppiclient.Globals.TITLE_GRAPH;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableFragment extends Fragment {
    private static TableFragment mTableFragment;

    public TableFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        mTableFragment = this;
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if( item.getItemId() == R.id.menu_graph )
        {
            String title = TITLE_GRAPH;
//            Fragment fragment = ( GraphFragment.getGraphFragmentInstance() == null ?
//                    new GraphFragment() : GraphFragment.getGraphFragmentInstance() );
            Fragment fragment = new GraphFragment();
            getActivity().setTitle(title);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    static Fragment getTableFragmentInstance() {
        return mTableFragment;
    }
}

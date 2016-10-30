package com.jainamjhaveri.rasppiclient;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jainamjhaveri.rasppiclient.Graph.GraphFragment;
import com.jainamjhaveri.rasppiclient.Table.TableFragment;
import com.jainamjhaveri.rasppiclient.Utils.CustomViewPager;
import com.jainamjhaveri.rasppiclient.Utils.DataPoint;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jainamjhaveri.rasppiclient.Utils.Globals.ch1;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.ch2;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.channelArray;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.currentChannel;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.isPubConfigInitialized;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.subscribeKey;
import static com.jainamjhaveri.rasppiclient.Utils.Globals.xindex;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    CustomViewPager viewPager;
    MainFragmentAdapter adapter;

    private final String TAG = this.getClass().getSimpleName();
    private static ArrayList<DataPoint> list1 = new ArrayList<>();
    private static ArrayList<DataPoint> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if ( !isPubConfigInitialized ) {
            initPubNubConfig();
            isPubConfigInitialized = true;
        }

        getReferences();
        initToolbar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary) );
        setupTabs(tabLayout);
    }

    private void setupTabs(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(R.drawable.graph);
        tabLayout.getTabAt(1).setIcon(R.drawable.table);
    }

    private void setupViewPager(CustomViewPager viewPager) {
        adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFrag(new GraphFragment(), "Graph");
        adapter.addFrag(new TableFragment(), "Table");
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
    }

    private void getReferences() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (CustomViewPager) findViewById(R.id.pager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private class MainFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        MainFragmentAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        // to display title
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }


    }

    private void initPubNubConfig() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("");
        pnConfiguration.setSubscribeKey(subscribeKey);
        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                if (status.getOperation() == PNOperationType.PNSubscribeOperation) {
                    Log.e(TAG, "status: subscribe operation");
                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                        Log.e(TAG, "status: connected !");
                    }
                }
            }

            public void message(PubNub pubnub, PNMessageResult message) {

                float recievedPoint = (float) message.getMessage().asDouble();
                String channel = message.getChannel();
                long time = System.currentTimeMillis();     // TODO: get time servertime from timetoken and convert it to IST
                Log.e(TAG, "message: " + recievedPoint);

                DataPoint object = new DataPoint(recievedPoint, time);

                if(channel.equals(ch1)){
                    list1.add(object);
                }
                else{
                    list2.add(object);
                }

//                TableFragment.getInstance().updateTable(object);
                TableFragment.getInstance().updateTable();
                GraphFragment.updateGraph(object);

                xindex++;
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }

        });

        pubnub.subscribe().channels(Arrays.asList(channelArray)).execute();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.graphmenu, menu);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sensor1:
                currentChannel = ch1;
                break;
            case R.id.menu_sensor2:
                currentChannel = ch2;
                break;
            default:
                Log.e(TAG, "onOptionsItemSelected: cant reach here");
        }
        if(TableFragment.getInstance() == null) return false;
        TableFragment.getInstance().updateTable();
//        GraphFragment.updateGraph();
        return true;
    }


    public static ArrayList<DataPoint> getArrayList() {
        switch (currentChannel){
            case ch1:
                return list1;
            case ch2:
                return list2;
        }
        System.out.println("MainAc.. getArrayList: cant reach here");
        return null;
    }


}
package com.jainamjhaveri.rasppiclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

import static com.jainamjhaveri.rasppiclient.Globals.TITLE_GRAPH;
import static com.jainamjhaveri.rasppiclient.Globals.subscribeKey;

public class MainActivity extends AppCompatActivity {


    private final String TAG = this.getClass().getSimpleName();
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
// TODO: After restarting app the data should be preserved
        addInitialGraphFragment();
        initToolbar();
        initPubNubConfig();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void addInitialGraphFragment() {
        if (fragment == null)
        {
            fragment = new GraphFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
        }
        setTitle(TITLE_GRAPH);
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

                Log.e(TAG, "message: " + recievedPoint);

                // TODO: get time somehow and not from timetoken
                GraphFragment.getGraphFragmentInstance()
                                .addGraphEntry(recievedPoint, System.currentTimeMillis());

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }

        });
        pubnub.subscribe().channels(Arrays.asList("my_channel")).execute();
    }

}
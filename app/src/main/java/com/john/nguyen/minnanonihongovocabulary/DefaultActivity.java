package com.john.nguyen.minnanonihongovocabulary;
import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.john.nguyen.minnanonihongovocabulary.receiver.NetworkChangeReceiver;

public class DefaultActivity extends AppCompatActivity {

    protected Activity mActivity;

    private NetworkChangeReceiver mNetworkChangeReceiver = null;
    private IntentFilter mIntentFilter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNetworkChangeReceiver != null) {
            registerReceiver(mNetworkChangeReceiver, mIntentFilter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }
}

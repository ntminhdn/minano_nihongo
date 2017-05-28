package com.john.nguyen.minnanonihongovocabulary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import utils.Internet;
import utils.Util;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean sIsCurrentStatus = true;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean isAvailable = Internet.isInternetAvailable(context);
        if(sIsCurrentStatus != isAvailable){
            Util.LOG("NetworkChangeReceiver: Refresh Admob");
            sIsCurrentStatus = isAvailable;
            //AdmobManagement.getInstance().refresh();
        }
    }
}
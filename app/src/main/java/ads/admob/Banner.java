package ads.admob;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import utils.Util;

public class Banner {

    private static Banner instance = null;

    private AdRequest mAdRequest;
    AdView mAdView;


    private Banner(){
        mAdRequest = new AdRequest.Builder().build();
    }

    public static Banner getInstance(){
        if(instance == null){
            instance = new Banner();
        }
        return instance;
    }

    public void load(Activity activity, int adViewId){
        mAdView = (AdView) activity.findViewById(adViewId);
        mAdView.setVisibility(View.GONE);
        mAdView.loadAd(mAdRequest);

        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Util.LOG("onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Util.LOG("onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Util.LOG("onAdLoaded");
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void reLoad(){
        if(mAdView.getVisibility() != View.VISIBLE) {
            mAdView.loadAd(mAdRequest);
        }
    }
}

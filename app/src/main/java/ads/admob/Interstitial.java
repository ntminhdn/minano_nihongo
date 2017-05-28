package ads.admob;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import utils.Util;


public class Interstitial {

    private static Interstitial instance = null;

    private static Activity mActivity = null;
    private static String mUnitId = null;

    private InterstitialAd mInterstitialAd;


    public static void init(Activity activity, String unitId){
        mActivity = activity;
        mUnitId = unitId;
    }

    private Interstitial(){
        if(mActivity == null){
            throw new RuntimeException("Please call static init method to first");
        }
        mInterstitialAd = new InterstitialAd(mActivity);
        mInterstitialAd.setAdUnitId(mUnitId);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Util.LOG("onAdClosed");
                requestNewInterstitial();
            }

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
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Util.LOG("onAdLeftApplication");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Util.LOG("onAdLoaded");
            }
        });
        requestNewInterstitial();
    }

    public static Interstitial getInstance(){
        if(instance == null){
            instance = new Interstitial();
        }
        return instance;
    }

    public void show(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }

    public void reLoad(){
        if(!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
    }

    private void requestNewInterstitial(){
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

}

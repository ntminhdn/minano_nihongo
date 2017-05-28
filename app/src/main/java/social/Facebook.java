package social;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.john.nguyen.minnanonihongovocabulary.BuildConfig;
import com.john.nguyen.minnanonihongovocabulary.R;
import utils.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Facebook {

    private static Activity mActivity;
    private static CallbackManager mCallbackManager;
    private static ShareDialog mShareDialog;

    //Must call before setContentView. Should call in onStart()
    public static void init(Activity activity){

        mActivity = activity;

        FacebookSdk.sdkInitialize(mActivity);

        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(mActivity);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Util.LOG("FACEBOOK: Login onSuccess");

            }

            @Override
            public void onCancel() {
                Util.LOG("FACEBOOK: Login onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Util.LOG("FACEBOOK: Login onError");
            }
        });

        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Util.LOG("FACEBOOK: Sharer onSuccess");
                Util.showToast(mActivity, R.string.successful);
            }

            @Override
            public void onCancel() {
                Util.LOG("FACEBOOK: Sharer onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Util.LOG("FACEBOOK: Sharer onError");
                Util.showToast(mActivity, R.string.failed);
            }
        });

        Util.LOG("Keyhash: " + getKeyHash());
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        Util.LOG("FACEBOOK: onActivityResult resultCode = " + resultCode);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static void appEventsLoggerActivateApp() {
        AppEventsLogger.activateApp(mActivity);
    }

    public static void appEventsLoggerDeactivateApp() {
        AppEventsLogger.deactivateApp(mActivity);
    }

    public static String getKeyHash(){
        String keyHash = "";
        try {
            PackageInfo info = mActivity.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = (Base64.encodeToString(md.digest(), Base64.DEFAULT)).toString();
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return keyHash;
    }

    public static void share(String title, String description, String url){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(description)
                    .setContentUrl(Uri.parse(url))
                    .build();
            mShareDialog.show(linkContent);
        }
    }

    public static void logOut(){
        LoginManager.getInstance().logOut();
    }

    public static void LogIn(){
        LogIn(mActivity);
    }

    public static void LogIn(Activity activity){
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends"));
    }

    public static boolean isConnected(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static String getFaceBookId(){
        return Profile.getCurrentProfile().getId();
    }
}

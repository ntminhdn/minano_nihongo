package utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class Device {

    public static final String LANGUAGE_NONE = "none";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_VI = "vi";

    private static Device sInstance;
    private String mLanguage;

    public static Device getInstance(){
        if(sInstance == null)
            sInstance = new Device();
        return sInstance;
    }

    public void setLanguage(Context context, String language){
        if(language.equals(mLanguage))
            return;

        mLanguage = language;
        if(mLanguage.equals(LANGUAGE_NONE)){ //not set language, detect language of device
            //get device language
            mLanguage = getDeviceLanguage();
            if(!mLanguage.equals(LANGUAGE_VI))
                mLanguage = "en";
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context);
        }
        else{
            updateResourcesLegacy(context);
        }
    }

    public String getLanguage(){
        return mLanguage;
    }

    public String getDeviceLanguage(){
        System.out.println("device language: " + Locale.getDefault().getLanguage());
        return Locale.getDefault().getLanguage();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void updateResources(Context context) {
        Locale locale = new Locale(mLanguage);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private void updateResourcesLegacy(Context context) {
        Locale locale = new Locale(mLanguage);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}


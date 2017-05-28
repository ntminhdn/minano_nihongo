package com.john.nguyen.minnanonihongovocabulary.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import utils.Device;
import utils.Util;

/**
 * Created by dvan on 2/15/2017.
 */

public class RMS {
    private Context mContext;
    private SharedPreferences mSharePreference;
    private static RMS sInstance;

    private int mCurrentLessonIndex = 0;
    private int[] mVersionLessons;
    private int mTotalLesson = 0;
    private int mVersionApp = 0;
    private int mLaunch_app = 0;
    private boolean mIsRated = false;
    private String mLanguage = Device.LANGUAGE_NONE;

    private final String CURRENT_LESSON_INDEX = "currentLessonIndex";
    private final String VERSION_LESSON = "versionLesson";
    private final String TOTAL_LESSON = "totalLesson";
    private final String VERSION_APP = "version";
    private final String LAUNCH_APP = "launch_app";
    private final String RATED = "rated";
    private final String LANGUAGE = "language";

    public static RMS getInstance() {
        if (sInstance == null)
            sInstance = new RMS();
        return sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        mSharePreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setNumberOfLaunchApp(int value) {
        mLaunch_app = value;
    }

    public void increaseNumberOfLaunchApp() {
        mLaunch_app++;
        Util.LOG("Save mLaunch_app = " + mLaunch_app);
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putInt(LAUNCH_APP, mLaunch_app);
        editor.commit();
    }

    public boolean isDownloadData(){
        if(isFirstLaunchApp() || mTotalLesson == 0 || mVersionApp == 0){
            return true;
        }
        return !false;
    }

    public int getNumberOfLaunchApp() {
        return mLaunch_app;
    }

    public boolean isFirstLaunchApp() {
        return mLaunch_app == 0;
    }

    public boolean isRated() {
        return mIsRated;
    }

    public void setRated(boolean value) {
        mIsRated = value;
    }

    public void saveRated() {
        Util.LOG("Save mIsRated = " + mIsRated);
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putBoolean(RATED, mIsRated);
        editor.commit();
    }

    public void setLanguage(String value) {
        mLanguage = value;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void saveLanguage() {
        Util.LOG("Save mLanguage = " + mLanguage);
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putString(LANGUAGE, mLanguage);
        editor.commit();
    }

    /**
     * getVersion is version of Total data
     * @return
     */
    public int getVersion() {
        return mVersionApp;
    }

    public void setVersion(int version) {
        this.mVersionApp = version;
    }

    public void saveVersion() {
        Util.LOG("Save mVersionApp = " + mVersionApp);
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putInt(VERSION_APP, mVersionApp);
        editor.commit();
    }

    public int getTotalLesson() {
        return mTotalLesson;
    }

    public void setTotalLesson(int totalVersion) {
        mTotalLesson = totalVersion;
        mVersionLessons = new int[mTotalLesson];
    }

    public void saveTotalLesson() {
        Util.LOG("Save mTotalLesson = " + mTotalLesson);
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putInt(TOTAL_LESSON, mTotalLesson);
        editor.commit();
    }

    public int[] getVersionLessons(){
        return mVersionLessons;
    }

    public void loadVersionLessons(){
        for(int i = 0; i < mTotalLesson; i++){
            mVersionLessons[i] = mSharePreference.getInt((VERSION_LESSON + i), 0);
        }
    }

    public int getVersionLesson(int lessonIndex){
        if(lessonIndex >=0 && lessonIndex < mTotalLesson) {
            return mVersionLessons[lessonIndex];
        }
        else{
            Util.LOG("Out of Lesson Index. Please check again");
            return 0;
        }
    }

    public void saveVersionLessons(){
        String versionOfLessons = "";
        for(int i = 0; i < mTotalLesson; i++){
            versionOfLessons += "lesson" + i + ":" + mVersionLessons[i] + ", ";
        }
        Util.LOG("Save mVersionLessons  = " + versionOfLessons);

        SharedPreferences.Editor editor = mSharePreference.edit();
        for(int i = 0; i < mTotalLesson; i++){
            editor.putInt((VERSION_LESSON + i), mVersionLessons[i]);
        }
        editor.commit();
    }

    public void saveVersionLesson(int lessonIndex, int version){
        if(lessonIndex >=0 && lessonIndex < mTotalLesson) {
            mVersionLessons[lessonIndex] = version;
            Util.LOG("Save Version of Lesson " + lessonIndex + " = " + mVersionLessons[lessonIndex]);
            SharedPreferences.Editor editor = mSharePreference.edit();
            editor.putInt((VERSION_LESSON + lessonIndex), mVersionLessons[lessonIndex]);
            editor.commit();
        }
    }

    public void setVersionLesson(int lessonIndex, int version){
        if(lessonIndex >=0 && lessonIndex < mTotalLesson) {
            Util.LOG("setVersionLesson lesson: " + lessonIndex + " verion: " + version);
            mVersionLessons[lessonIndex] = version;
        }
    }

    public void setCurrentLessonIndex(int index) {
        mCurrentLessonIndex = index;
    }

    public int getCurrentLessonIndex() {
        return mCurrentLessonIndex;
    }

    public void saveCurrentLessonIndex() {
        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putInt(CURRENT_LESSON_INDEX, mCurrentLessonIndex);
        editor.commit();
    }

    public void load() {
        mLaunch_app = mSharePreference.getInt(LAUNCH_APP, mLaunch_app);
        mIsRated = mSharePreference.getBoolean(RATED, mIsRated);
        mLanguage = mSharePreference.getString(LANGUAGE, Device.LANGUAGE_NONE);
        mVersionApp = mSharePreference.getInt(VERSION_APP, mVersionApp);
        mTotalLesson = mSharePreference.getInt(TOTAL_LESSON, mTotalLesson);
        mCurrentLessonIndex = mSharePreference.getInt(CURRENT_LESSON_INDEX, mCurrentLessonIndex);

        mVersionLessons = new int[mTotalLesson];
        for(int i = 0; i < mTotalLesson; i++){
            mVersionLessons[i] = mSharePreference.getInt((VERSION_LESSON + i), 0);
        }

        Util.LOG("----------------Load RMS---------------");
        Util.LOG("mLaunch_app          = " + mLaunch_app);
        Util.LOG("mIsRated             = " + mIsRated);
        Util.LOG("mLanguage            = " + mLanguage);
        Util.LOG("mVersionApp          = " + mVersionApp);
        Util.LOG("mTotalLesson         = " + mTotalLesson);
        Util.LOG("mCurrentLessonIndex  = " + mCurrentLessonIndex);
        String versionOfLessons = "";
        for(int i = 0; i < mTotalLesson; i++){
            versionOfLessons += "lesson" + i + ":" + mVersionLessons[i] + ", ";
        }
        Util.LOG("mVersionLessons  = " + versionOfLessons);
    }

    public void save() {
        Util.LOG("----------------SAVE RMS---------------");
        Util.LOG("mLaunch_app          = " + mLaunch_app);
        Util.LOG("mIsRated             = " + mIsRated);
        Util.LOG("mLanguage            = " + mLanguage);
        Util.LOG("mVersionApp          = " + mVersionApp);
        Util.LOG("mTotalLesson         = " + mTotalLesson);
        Util.LOG("mCurrentLessonIndex  = " + mCurrentLessonIndex);
        String versionOfLessons = "";
        for(int i = 0; i < mTotalLesson; i++){
            versionOfLessons += "lesson" + i + ":" + mVersionLessons[i] + ", ";
        }
        Util.LOG("mVersionLessons  = " + versionOfLessons);

        SharedPreferences.Editor editor = mSharePreference.edit();
        editor.putInt(LAUNCH_APP, mLaunch_app);
        editor.putBoolean(RATED, mIsRated);
        editor.putString(LANGUAGE, mLanguage);
        editor.putInt(VERSION_APP, mVersionApp);
        editor.putInt(TOTAL_LESSON, mTotalLesson);
        editor.putInt(CURRENT_LESSON_INDEX, mCurrentLessonIndex);
        for(int i = 0; i < mTotalLesson; i++){
            editor.putInt((VERSION_LESSON + i), mVersionLessons[i]);
        }
        editor.commit();
    }
}

package com.john.nguyen.minnanonihongovocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.john.nguyen.minnanonihongovocabulary.controller.DownloadListener;
import com.john.nguyen.minnanonihongovocabulary.controller.DownloadManagement;
import com.john.nguyen.minnanonihongovocabulary.controller.LessonManagement;
import com.john.nguyen.minnanonihongovocabulary.models.RMS;
import com.john.nguyen.minnanonihongovocabulary.sqlitehelper.SqliteHelper;


import social.Facebook;
import utils.Util;

public class MainActivity extends DefaultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirst();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Util.exitApp();
    }

    public void functionProcess(View view){
        switch (view.getId()){
            case R.id.btnLearn:
                Intent intentLesson = new Intent(MainActivity.this, LessonActivity.class);
                startActivity(intentLesson);
                break;
            case R.id.btnPractice:
                Intent intentPractice = new Intent(MainActivity.this, PracticeActivity.class);
                startActivity(intentPractice);
                break;
            case R.id.btnSearch:
                Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intentSearch);
                break;
        }
    }

    //------------------------------------------
    private void initFirst() {
        //faceook
        Facebook.init(this);
        SqliteHelper.init(this);

        //rms
        RMS rms = RMS.getInstance();
        rms.init(this);
        rms.load();

        DownloadManagement.init(this);
        DownloadManagement.getInstance().setListener(new DownloadListener() {
            @Override
            public void onStatus(int status) {
                switch (status){
                    case DownloadListener.STATUS_NONE:
                    case DownloadListener.STATUS_FINISHED:
                        start();
                        break;
                    case DownloadListener.STATUS_MINIMIZE:
                        moveTaskToBack(true);
                        break;
                    case DownloadListener.STATUS_EXIT:
                        Util.exitApp();
                        break;
                }
            }
        });
        DownloadManagement.getInstance().checkingData();

    }

    private void start(){
        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    private void init() {
        RMS rms = RMS.getInstance();
        rms.increaseNumberOfLaunchApp();

        LessonManagement lessonManagement = LessonManagement.getInstance();
        lessonManagement.setNumberLesson(rms.getTotalLesson());
        lessonManagement.loadLessons();
    }

    private void getWidgets() {
    }

    private void setWidgets() {
    }

    private void addWidgetsListener() {
    }

}

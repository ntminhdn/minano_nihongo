package com.john.nguyen.minnanonihongovocabulary.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.john.nguyen.minnanonihongovocabulary.R;
import com.john.nguyen.minnanonihongovocabulary.models.RMS;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import utils.Internet;
import utils.Util;

/**
 * Created by Administrator on 18/03/2017.
 */

public class DownloadManagement {

    private final String URL_LESSON = "https://www.dropbox.com/s/mxcm3lhja0kq6lv/lessons.json?dl=1";
    private final String KEY_LESSON_VERSION = "version";
    private final String KEY_LESSON_LESSONS = "lesson";
    private final String KEY_LESSON_LINK = "link";
    private final String KEY_LESSON_VOCABULARYS = "vocabulary";

    private final String mExtension = ".zip";
    private final String mSubFolder = "lesson";
    private final String mContentFileName = "content.json";

    private final int DATA_DOWNLOAD_PROCESS = 0;
    private final int DATA_UPDATE_PROCESS = 1;

    private static DownloadManagement sInstance = null;
    private static Context sContext;

    public final int WHAT_CHECKING_DATA = 0;
    public final int WHAT_START_UPDATE = 1;
    public final int WHAT_DOWLOADING_LESSON = 2;
    public final int WHAT_EXTRACTING_LESSON = 3;
    public final int WHAT_DOWNLOAD_ERROR = 4;
    public final int WHAT_DOWNLOAD_FINISHED = 5;

    private String mPathSaved = "";
    private String mPathExtract = "";
    private int mLessonIndex;
    private int mLessonVersion;
    private boolean mIsDonwloading = true;

    private int mDataCheckingType;

    //Dialog
    private ProgressDialog mProgressDialog;
    private Handler mHandlerThread;
    private DownloadListener mListener;

    private DownloadManagement(){
        if(sContext == null){
            Util.LOG("Please call init method of DownloadManagement first");
        }
        else {
            mPathSaved = sContext.getExternalFilesDir(null).getAbsolutePath();
            mPathExtract = mPathSaved;
            Util.LOG("Init mPathSaved: " + mPathSaved);
            Util.LOG("Init mPathExtract: " + mPathExtract);

            mProgressDialog = new ProgressDialog(sContext);
            mProgressDialog.setCancelable(false);
            mHandlerThread = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    updateUI(msg);
                }
            };
        }
    }

    public static void init(Context context){
        sContext = context;
    }

    public static DownloadManagement getInstance(){
        if(sInstance == null){
            sInstance = new DownloadManagement();
        }
        return sInstance;
    }

    public void setListener(DownloadListener listener){
        mListener = listener;
    }


    public void checkingData(){
        if(RMS.getInstance().isDownloadData() ||LessonManagement.getInstance().isUpdateData()){
            mDataCheckingType = DATA_DOWNLOAD_PROCESS;
        }
        else{
            mDataCheckingType = DATA_UPDATE_PROCESS;
            //Inform to Main
            if(mListener != null){
                mListener.onStatus(DownloadListener.STATUS_NONE);
            }
        }


        if(Internet.isInternetAvailable(sContext)) {
            download(URL_LESSON);
        }
        else{
            showNoInternetDownloadPopup();
        }
    }

    //------------------------------------------------
    private void updateUI(Message msg){
        switch (msg.what){
            case WHAT_CHECKING_DATA:
                mProgressDialog.setMessage(sContext.getString(R.string.checking_data));
                mProgressDialog.show();
                break;
            case WHAT_START_UPDATE:
                mProgressDialog.setTitle(R.string.update_data);
                mProgressDialog.setMessage("");
                mProgressDialog.show();
                break;
            case WHAT_DOWLOADING_LESSON:
                mProgressDialog.setMessage(sContext.getString(R.string.download) + " "
                        + sContext.getString(R.string.lesson).toLowerCase() + " "
                        + (mLessonIndex + 1) + ": "
                        + msg.arg1 + "%");
                break;
            case WHAT_EXTRACTING_LESSON:
                mProgressDialog.setMessage(sContext.getString(R.string.extract) + " "
                        + sContext.getString(R.string.lesson).toLowerCase() + " "
                        + (mLessonIndex + 1) + ": "
                        + msg.arg1 + "%");
                break;
            case WHAT_DOWNLOAD_ERROR:
                //Has error the same check and download continue
                RMS.getInstance().setVersion(0);
                mProgressDialog.dismiss();
                showNoInternetDownloadPopup();

                break;
            case WHAT_DOWNLOAD_FINISHED:
                mProgressDialog.dismiss();
                //Inform to Main
                if(mListener != null){
                    mListener.onStatus(DownloadListener.STATUS_FINISHED);
                }
                break;
        }

    }

    private void download(final String urlInput) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    if(mDataCheckingType == DATA_DOWNLOAD_PROCESS) {
                        Message msg = new Message();
                        msg.what = WHAT_CHECKING_DATA;
                        mHandlerThread.sendMessage(msg);
                    }

                    String jsonString = "";
                    URL url = new URL(urlInput);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader mReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuffer buffer = new StringBuffer();
                    String line;

                    while ((line = mReader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    jsonString = buffer.toString();
                    //Util.LOG(jsonString);
                    JSONObject jsonObject = new JSONObject(jsonString);

                    //download Lessons here
                    if(mDataCheckingType == DATA_DOWNLOAD_PROCESS) {
                        processDownloadContent(jsonObject);

                    }
                    else if(mDataCheckingType == DATA_UPDATE_PROCESS){
                        if(isUpdateData(jsonObject)){
                            Message msg = new Message();
                            msg.what = WHAT_START_UPDATE;
                            mHandlerThread.sendMessage(msg);
                            processDownloadContent(jsonObject);
                        }
                    }

                    //update UI
                    Message msg = new Message();
                    msg.what = WHAT_DOWNLOAD_FINISHED;
                    mHandlerThread.sendMessage(msg);

                    mReader.close();
                    httpURLConnection.disconnect();
                } catch (Exception ex) {
                    mIsDonwloading = false;
                    Message msg = new Message();
                    msg.what = WHAT_DOWNLOAD_ERROR;
                    mHandlerThread.sendMessage(msg);

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    Util.LOG("Download Lesson Error" + ex.toString());

                }
            }
        }).start();
    }

    private void processDownloadContent(JSONObject jsonObject){
        try {
            int version = jsonObject.getInt(KEY_LESSON_VERSION);
            JSONArray lessonArray = jsonObject.getJSONArray(KEY_LESSON_LESSONS);
            final int totalLesson = lessonArray.length();

            RMS rms = RMS.getInstance();
            rms.setTotalLesson(totalLesson);
            rms.setVersion(version);
            rms.loadVersionLessons(); //load again

            for(int i = 0; i < totalLesson; i++){
                mLessonIndex = i;
                JSONObject lessonJson = lessonArray.getJSONObject(i);
                mLessonVersion = lessonJson.getInt(KEY_LESSON_VERSION);
                String link = lessonJson.getString(KEY_LESSON_LINK);

                //just download version > currentVersion : update ==> Download error and start download again, skip lesson downloaded
                if(RMS.getInstance().getVersionLesson(mLessonIndex) < mLessonVersion) {
                    String saveTo = mPathSaved + "/" + String.valueOf(mLessonIndex + 1) + mExtension;
                    Util.LOG("Start download link: " + link);
                    Util.LOG("Version Of Lesson: " + mLessonVersion);
                    Util.LOG("Save to: " + saveTo);
                    //start download lesson
                    downloadLesson(link, saveTo);
                    extractLesson(saveTo);

                    rms.saveVersionLesson(mLessonIndex, mLessonVersion);
                }
                if(!mIsDonwloading){
                    break;
                }
            }

            rms.saveTotalLesson();
            rms.saveVersion();
        }
        catch (Exception ex){
            Util.LOG(ex.toString());
        }
    }

    private boolean isUpdateData(JSONObject jsonObject){
        try {
            RMS rms = RMS.getInstance();
            int version = jsonObject.getInt(KEY_LESSON_VERSION);
            JSONArray lessonArray = jsonObject.getJSONArray(KEY_LESSON_LESSONS);
            final int totalLesson = lessonArray.length();

            if(rms.getVersion() < version){
                return true;
            }

            for(int i = 0; i < totalLesson; i++){
                JSONObject lessonJson = lessonArray.getJSONObject(i);
                if(rms.getVersionLesson(i) < lessonJson.getInt(KEY_LESSON_VERSION)){
                    return true;
                }
            }
        }
        catch (Exception ex){
            Util.LOG(ex.toString());
            return false;
        }
        return false;
    }


    private void downloadLesson(final String urlInput, final String urlOutput){
        //update UI
        Message msgStartDownload = new Message();
        msgStartDownload.what = WHAT_DOWLOADING_LESSON;
        msgStartDownload.arg1 = 0;
        mHandlerThread.sendMessage(msgStartDownload);
        HttpURLConnection httpURLConnection = null;
        try
        {
            URL url  = new URL(urlInput);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            long totalSize = httpURLConnection.getContentLength();
            long downloadedSize = 0;

            byte[] buffer = new byte[1024];
            int bufferLength = 0; //used to store a temporary size of the buffer

            //out put
            FileOutputStream fos = new FileOutputStream(urlOutput);

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;

                //Util.LOG(downloadedSize + "/" + totalSize);
                //update UI
                Message msg = new Message();
                msg.what = WHAT_DOWLOADING_LESSON;
                msg.arg1 = (int)(downloadedSize*100/totalSize);
                mHandlerThread.sendMessage(msg);
            }

            httpURLConnection.disconnect();
            fos.close();
        }
        catch (Exception e)
        {
            mIsDonwloading = false;
            Message msgError = new Message();
            msgError.what = WHAT_DOWNLOAD_ERROR;
            mHandlerThread.sendMessage(msgError);

            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            Util.LOG("Download Lesson Error" + e.toString());
        }
    }

    private void extractLesson(String pathSaved){
        Util.LOG("pathSaved " + pathSaved);
        //update UI
        Message msgStartExtract = new Message();
        msgStartExtract.what = WHAT_EXTRACTING_LESSON;
        msgStartExtract.arg1 = 0;
        mHandlerThread.sendMessage(msgStartExtract);

        try{

            //Unzip
            Util.extractZipFiles(pathSaved, mPathExtract);

            //delete
            Util.deleteFile(pathSaved);

            //Parse and save database
            String lessonContentPath = mPathExtract + "/" + mSubFolder + (mLessonIndex + 1) + "/" + mContentFileName; //data start from 1 to n
            Util.LOG("parse lessonContentPath: " + lessonContentPath);
            String content = Util.readFile(lessonContentPath);

            //delete
            Util.deleteFile(lessonContentPath);

            VocabularyOpearation vocabularyOpearation = VocabularyOpearation.getInstance();
            JSONObject jsonObject = new JSONObject(content);
            JSONArray vocabularyJsonArray = jsonObject.getJSONArray(KEY_LESSON_VOCABULARYS);

            //delete all vocabulary of lesson first;
            vocabularyOpearation.delete(mLessonIndex);

            //add new
            for(int i = 0; i < vocabularyJsonArray.length(); i++){
                JSONObject vocabularyJson = vocabularyJsonArray.getJSONObject(i);
                String word = vocabularyJson.getString(Vocabulary.FIELD_WORD);
                String kanji = vocabularyJson.getString(Vocabulary.FIELD_KANJI);
                String mean_en = vocabularyJson.getString(Vocabulary.FIELD_MEAN_EN);
                String mean_vi = vocabularyJson.getString(Vocabulary.FIELD_MEAN_VI);
                String sound = mPathExtract + vocabularyJson.getString(Vocabulary.FIELD_SOUND);
                vocabularyOpearation.add(word, kanji, mean_en, mean_vi, sound, mLessonIndex); //just make sure lesson saved into database start from 0 to n


                //update UI
                Message msg = new Message();
                msg.what = WHAT_EXTRACTING_LESSON;
                msg.arg1 = 100*i/vocabularyJsonArray.length();
                mHandlerThread.sendMessage(msg);
            }
        }
        catch (Exception ex){
            Util.LOG(ex.toString());
        }
    }

    private void showNoInternetDownloadPopup(){
        new AlertDialog.Builder(sContext)
                .setTitle(R.string.download)
                .setMessage(R.string.internet_no_internet_download)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK){
                            //Inform to Main
                            if(mListener != null){
                                mListener.onStatus(DownloadListener.STATUS_MINIMIZE);
                            }
                        }
                        return false;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Internet.isInternetAvailable(sContext)){
                            checkingData();
                        }
                        else{
                            showNoInternetDownloadPopup();
                        }
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Inform to Main
                        if(mListener != null){
                            mListener.onStatus(DownloadListener.STATUS_EXIT);
                        }
                    }
                })
                .create().show();


    }

}

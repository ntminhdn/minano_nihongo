package com.john.nguyen.minnanonihongovocabulary.controller;

import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 19/02/2017.
 */

public class LessonManagement {

    private static LessonManagement sInstance = null;

    private int mTotalLesson;
    private String[] mLessonArrayString = null;
    private VocabularyOpearation mVocabularyOpearation = null;
    private HashMap<Integer, ArrayList<Vocabulary>>  mLessons;

    private LessonManagement(){
        mVocabularyOpearation = VocabularyOpearation.getInstance();
        mLessons = new HashMap();

        mTotalLesson = -1;
    }

    public static LessonManagement getInstance(){
        if(sInstance == null){
            sInstance = new LessonManagement();
        }
        return sInstance;
    }

    public void setNumberLesson(int num){
        mTotalLesson = num;
    }

    public int getNumberLesson(){
        return mTotalLesson;
    }

    public String[] getLessonArrayString(String lessonName){
        if(mLessonArrayString == null){
            mLessonArrayString = new String[mTotalLesson];
            for(int i = 0; i < mTotalLesson; i++){
                mLessonArrayString[i] = lessonName + " " + (i + 1);
            }
        }
        return mLessonArrayString;
    }

    public void loadLessons(){
        if(mTotalLesson < 0){
            throw new RuntimeException("Please call setNumberLesson method to set number of Lesson");
        }

        for(int i = 0; i < mTotalLesson; i++){
            ArrayList<Vocabulary> lesson = mVocabularyOpearation.get(i);
            mLessons.put(i, lesson);
        }
    }

    //0, 1, ...., numberLesson
    public ArrayList<Vocabulary> getLesson(int lessonId){
        if(lessonId > mTotalLesson){
            throw new RuntimeException("Current lessonID >  Number of Lesson. Please check lessonId again");
        }
        ArrayList<Vocabulary> lesson = mLessons.get(lessonId);
        if(lesson == null || lesson.isEmpty()){
            lesson = mVocabularyOpearation.get(lessonId);
            mLessons.put(lessonId, lesson);
        }
        return lesson;
    }

    public boolean isUpdateData(){
        for(int i = 0; i < mTotalLesson; i++){
            if(getLesson(i).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public void fakeData(){
        mVocabularyOpearation.fakeData(mTotalLesson);
    }
}

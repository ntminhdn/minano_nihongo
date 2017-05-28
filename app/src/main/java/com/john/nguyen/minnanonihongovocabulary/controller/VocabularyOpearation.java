package com.john.nguyen.minnanonihongovocabulary.controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;
import com.john.nguyen.minnanonihongovocabulary.sqlitehelper.SqliteHelper;

import java.util.ArrayList;

import utils.Util;

/**
 * Created by Administrator on 17/01/2017.
 */

public class VocabularyOpearation {

    private static VocabularyOpearation instance = null;

    private SqliteHelper mSqliteHelper;
    private SQLiteDatabase mSqLiteDatabase;

    private ArrayList<Vocabulary> mSearchList;

    private String[] VOCABULARY_COLUMNS = {
            Vocabulary.FIELD_ID,
            Vocabulary.FIELD_WORD,
            Vocabulary.FIELD_KANJI,
            Vocabulary.FIELD_MEAN_EN,
            Vocabulary.FIELD_MEAN_VI,
            Vocabulary.FIELD_SOUND,
            Vocabulary.FIELD_LESSON
    };

    private VocabularyOpearation() {
        mSqliteHelper = SqliteHelper.getInstance();
        mSearchList = new ArrayList<>();
    }

    public static VocabularyOpearation getInstance() {
        if (instance == null) {
            instance = new VocabularyOpearation();
        }
        return instance;
    }

    public ArrayList<Vocabulary> get(int lessonId) {
        ArrayList<Vocabulary> lesson = new ArrayList<>();
        mSqLiteDatabase = mSqliteHelper.getReadableDatabase();

        Cursor cursor = mSqLiteDatabase.query(SqliteHelper.TABLE_VOCABULARY, VOCABULARY_COLUMNS,
                Vocabulary.FIELD_LESSON + "=?", new String[]{String.valueOf(lessonId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Vocabulary.FIELD_ID));
                String word = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_WORD));
                String kanji = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_KANJI));
                String mean_en = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_MEAN_EN));
                String mean_vi = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_MEAN_VI));
                String sound = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_SOUND));
                Vocabulary vocabulary = new Vocabulary(id, word, kanji, mean_en, mean_vi, sound, lessonId);

                lesson.add(vocabulary);

                cursor.moveToNext();
            }
            cursor.close();
        }
        mSqLiteDatabase.close();
        mSqliteHelper.close();

        return lesson;
    }

    public ArrayList<Vocabulary> search(String keyword) {
        mSearchList.clear();
        mSqLiteDatabase = mSqliteHelper.getReadableDatabase();

        Cursor cursor = mSqLiteDatabase.query(SqliteHelper.TABLE_VOCABULARY, VOCABULARY_COLUMNS,
                Vocabulary.FIELD_MEAN_VI + " like '%" + keyword + "%'"
                + " or " + Vocabulary.FIELD_MEAN_EN + " like '%" + keyword + "%'"
                + " or " + Vocabulary.FIELD_KANJI + " like '%" + keyword + "%'"
                + " or " + Vocabulary.FIELD_WORD + " like '%" + keyword + "%'"
                , null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(Vocabulary.FIELD_ID));
                String word = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_WORD));
                String kanji = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_KANJI));
                String mean_en = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_MEAN_EN));
                String mean_vi = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_MEAN_VI));
                String sound = cursor.getString(cursor.getColumnIndex(Vocabulary.FIELD_SOUND));
                int lessonId = cursor.getInt(cursor.getColumnIndex(Vocabulary.FIELD_LESSON));
                Vocabulary vocabulary = new Vocabulary(id, word, kanji, mean_en, mean_vi, sound, lessonId);

                mSearchList.add(vocabulary);

                cursor.moveToNext();
            }
            cursor.close();
        }
        mSqLiteDatabase.close();
        mSqliteHelper.close();

        return mSearchList;
    }

    public void add(String word, String kanji, String mean_en, String mean_vi, String sound, int lesson) {

        mSqLiteDatabase = mSqliteHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(Vocabulary.FIELD_WORD, word);
        value.put(Vocabulary.FIELD_KANJI, kanji);
        value.put(Vocabulary.FIELD_MEAN_EN, mean_en);
        value.put(Vocabulary.FIELD_MEAN_VI, mean_vi);
        value.put(Vocabulary.FIELD_SOUND, sound);
        value.put(Vocabulary.FIELD_LESSON,lesson);

        mSqLiteDatabase.insert(SqliteHelper.TABLE_VOCABULARY, null, value);
        mSqLiteDatabase.close();
        mSqliteHelper.close();
    }

    public void delete(int lessonId) {
        mSqLiteDatabase = mSqliteHelper.getWritableDatabase();

        mSqLiteDatabase.delete(SqliteHelper.TABLE_VOCABULARY,
                Vocabulary.FIELD_LESSON + "= ?", new String[]{String.valueOf(lessonId)});

        mSqLiteDatabase.close();
        mSqliteHelper.close();
    }

    public void fakeData(int numberLesson){
        for(int i = 0; i < numberLesson; i++){
            for(int j = 0; j < 100; j++){
                String character = "" + (char)(i + 65); //start from A
                String word = character + (j + 1) + "";
                String kanji = "K_" + word;
                String mean_en = "EN_" + word;
                String mean_vi = "VI_" + word;
                String sound = "";
                if(i % 2 == 0) {
                    sound = "https://www.dropbox.com/s/hwv7dm14zkwe8km/002.mp3?dl=0";
                }
                else{
                    sound = "https://www.dropbox.com/s/dcqtf5fo1c1cebf/001.mp3?dl=0";
                }
                add(word, kanji, mean_en, mean_vi, sound, i);
                Util.LOG("Fake data: " + i + " " + word);
            }
        }

    }
}

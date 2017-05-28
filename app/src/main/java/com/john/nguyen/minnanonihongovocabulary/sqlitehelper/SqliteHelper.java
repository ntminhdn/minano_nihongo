package com.john.nguyen.minnanonihongovocabulary.sqlitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;


/**
 * Created by Administrator on 17/01/2017.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    private static SqliteHelper instance = null;
    private static Context mContext;

    private static final String DATABASE_NAME = "poiuytrew23567";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_VOCABULARY = "Vocabulary";

    private SqliteHelper() {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        if(mContext == null){
            throw new RuntimeException("Please call init method first." +
                    "Should call at Main Activity");
        }
    }

    public static SqliteHelper getInstance(){
        if(instance == null){
            instance = new SqliteHelper();
        }
        return instance;
    }

    public static void init(Context context){
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        deleteTables(sqLiteDatabase);
        createTables(sqLiteDatabase);
    }

    //------------------------
    private void createTables(SQLiteDatabase sqLiteDatabase){
        String sqlStr = "CREATE TABLE " + TABLE_VOCABULARY
                + " ( "
                + Vocabulary.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Vocabulary.FIELD_WORD + " TEXT, "
                + Vocabulary.FIELD_KANJI + " TEXT, "
                + Vocabulary.FIELD_MEAN_EN + " TEXT, "
                + Vocabulary.FIELD_MEAN_VI + " TEXT, "
                + Vocabulary.FIELD_SOUND + " TEXT, "
                + Vocabulary.FIELD_LESSON + " INTEGER "
                + " )";
        System.out.println(sqlStr);
        sqLiteDatabase.execSQL(sqlStr);
    }

    private void deleteTables(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_VOCABULARY);
    }

}

package com.john.nguyen.minnanonihongovocabulary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.john.nguyen.minnanonihongovocabulary.adapters.PracticeArrayAdapter;
import com.john.nguyen.minnanonihongovocabulary.controller.LessonManagement;
import com.john.nguyen.minnanonihongovocabulary.models.RMS;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;

import utils.ArrayRandom;
import utils.Sound;

public class PracticeActivity extends AppCompatActivity {

    private final int TOPICS_LIMIT_LENGTH = 30;

    private ArrayList<Vocabulary> mVocabularyList;
    private String [] mLessonArray;

    private LinearLayout mLayoutLesson;
    private TextView tvLesson;
    boolean []mSelectedLesson, mSelectedLessonTemp;

    private ViewPager vpVocabulary;
    private PracticeArrayAdapter mAdapter;
    private CheckBox cbShowWord, cbShowMean, cbShowKanji;
    private TextView tvCurrentOverTotal;
    private Button btnSound;
    private int mCurrentVocabularyIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    public void showHideChecking(View view){
        showViewPager(false);
    }

    //--------------------
    private void init(){
        mLessonArray = LessonManagement.getInstance().getLessonArrayString(getString(R.string.lesson));
        mSelectedLesson = new boolean[mLessonArray.length];

        //init select first topic
        mSelectedLesson[0] = true;
        mSelectedLessonTemp = new boolean[mSelectedLesson.length];
        mVocabularyList = new ArrayList<>();

        ArrayList<Vocabulary> firstLesson = LessonManagement.getInstance().getLesson(0);
        mVocabularyList.addAll(firstLesson);
    }

    private void getWidgets(){
        mLayoutLesson =(LinearLayout)findViewById(R.id.layoutLessons);
        tvLesson = (TextView)findViewById(R.id.tvTopics);
        vpVocabulary = (ViewPager) findViewById(R.id.vpVocabulary);

        cbShowWord = (CheckBox)findViewById(R.id.cbShowWord);
        cbShowMean = (CheckBox)findViewById(R.id.cbShowMean);
        cbShowKanji = (CheckBox)findViewById(R.id.cbShowKanji);
        tvCurrentOverTotal = (TextView)findViewById(R.id.tvCurrentOverTotal);
        btnSound = (Button)findViewById(R.id.btnSound);
    }

    private void setWidgets(){
        tvLesson.setText(mLessonArray[0]);
        showViewPager(true);
    }

    private void addWidgetsListener(){
        mLayoutLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceTopics();
            }
        });

        vpVocabulary.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentVocabularyIndex = position;
                updateCurrentOverTotal();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vocabulary vocabulary = mVocabularyList.get(mCurrentVocabularyIndex);
                Sound.getInstance().playSoundFromUrl(vocabulary.getSound());
            }
        });
    }

    private void showChoiceTopics(){
        //backup
        for(int i = 0; i < mSelectedLesson.length; i++){
            mSelectedLessonTemp[i] = mSelectedLesson[i];
        }

        View layout = getLayoutInflater().inflate(R.layout.dialog_title_checkbox, null);
        final TextView tvTitle = (TextView)layout.findViewById(R.id.tvTitle);
        final CheckBox cbSelectAll = (CheckBox)layout.findViewById(R.id.cbSelectAll);
        tvTitle.setText(R.string.lesson);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCustomTitle(layout);
        builder.setMultiChoiceItems(mLessonArray, mSelectedLesson, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //reverse if cancel
                for(int index = 0; index < mSelectedLesson.length; index++){
                    mSelectedLesson[index] = mSelectedLessonTemp[index];
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedTopics();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView listView = dialog.getListView();
                for(int i = 0; i < listView.getCount(); i++){
                    listView.setItemChecked(i, cbSelectAll.isChecked());
                    mSelectedLesson[i] = cbSelectAll.isChecked();
                }
            }
        });
    }

    private void selectedTopics(){
        //clean
        mVocabularyList.clear();

        String lessons = "";
        boolean isNoToppic = true;
        int firstChoice = 0;
        for(int index = 0; index < mSelectedLesson.length; index++){
            if(mSelectedLesson[index]){
                isNoToppic = false;
                firstChoice = index;
                break;
            }
        }

        //if no Topic select, Default select first Topic
        if(isNoToppic){
            mSelectedLesson[firstChoice] = true;
            lessons = mLessonArray[0];

            //add to List
            ArrayList<Vocabulary> firstTopic = LessonManagement.getInstance().getLesson(firstChoice);
            mVocabularyList.addAll(firstTopic);
        }
        else{
            lessons = getString(R.string.lesson) + ": " + (firstChoice + 1);
            //add to List
            ArrayList<Vocabulary> firstTopic = LessonManagement.getInstance().getLesson(firstChoice);
            mVocabularyList.addAll(firstTopic);
            for(int index = firstChoice + 1; index < mSelectedLesson.length; index++){
                if(mSelectedLesson[index]){
                    lessons += "-" + (index + 1);

                    //add to List
                    ArrayList<Vocabulary> topic = LessonManagement.getInstance().getLesson(index);
                    mVocabularyList.addAll(topic);
                }
            }
        }

        //cut. limit length
        if(lessons.length() > TOPICS_LIMIT_LENGTH) {
            lessons = lessons.substring(0, TOPICS_LIMIT_LENGTH) + "...";
        }
        tvLesson.setText(lessons);

        //re -show page
        showViewPager(true);
    }

    private void showViewPager(boolean isNew){
        //random array
        if(isNew) {
            mVocabularyList = ArrayRandom.get(mVocabularyList);
            mCurrentVocabularyIndex = 0;
        }
        mAdapter = new PracticeArrayAdapter(this, R.layout.item_vocabulary_practice, mVocabularyList,
                    cbShowWord.isChecked(), cbShowMean.isChecked(), cbShowKanji.isChecked(), RMS.getInstance().getLanguage());

        vpVocabulary.setAdapter(mAdapter);
        if(!isNew){
            vpVocabulary.setCurrentItem(mCurrentVocabularyIndex);
        }
        updateCurrentOverTotal();
    }

    private void updateCurrentOverTotal(){
        tvCurrentOverTotal.setText((mCurrentVocabularyIndex + 1) + "/" + mVocabularyList.size());
    }
}

package com.john.nguyen.minnanonihongovocabulary;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.john.nguyen.minnanonihongovocabulary.adapters.LearnArrayAdapter;
import com.john.nguyen.minnanonihongovocabulary.controller.LessonManagement;
import com.john.nguyen.minnanonihongovocabulary.models.RMS;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;

import utils.Util;

public class LearnActivity extends AppCompatActivity {

    private ListView mLvVocabulary;
    private LearnArrayAdapter mAdapter;
    private ArrayList<Vocabulary> mList;
    private int mCurrentLessonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    //--------------------
    private void init(){
        Intent intent = getIntent();
        mCurrentLessonIndex = intent.getIntExtra(LessonActivity.KEY_PASS_LESSON, 0);
        setTitle(getString(R.string.vocabularies) + " - " + (LessonManagement.getInstance().getLessonArrayString(getString(R.string.lesson))[mCurrentLessonIndex]));

        mList = LessonManagement.getInstance().getLesson(mCurrentLessonIndex);
        mAdapter = new LearnArrayAdapter(this, R.layout.item_vocabulary_learn, mList, RMS.getInstance().getLanguage());
    }

    private void getWidgets(){
        mLvVocabulary = (ListView)findViewById(R.id.lvVocabulary);
    }

    private void setWidgets(){
        mLvVocabulary.setAdapter(mAdapter);
    }

    private void addWidgetsListener(){

        mLvVocabulary.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //start test from 4
                /*
                if(position >= TestQuestion.ANSWER_COUNT - 1) {
                    showTestGuide(position);
                }
                */
                return false;
            }
        });
    }

    /*
    private void showTestGuide(final int at){
        String startWord = mList.get(0).getWord();
        String toWord = mList.get(at).getWord();

        String title = getString(R.string.test) + " " + getString(R.string.from) + " " + startWord + " " + getString(R.string.to) + " " + toWord;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setItems(R.array.test_type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<Vocabulary> vocabularies = new ArrayList<>();
                for(int index = 0; index <= at; index++){
                    vocabularies.add(mList.get(index));
                }
                switch (i) {
                    case MainActivity.TEST_MEMORY:
                        Intent memory = new Intent(LearnActivity.this, MemoyTestActivity.class);
                        memory.putExtra(MainActivity.KEY_PASS_VOCABULARIES, vocabularies);
                        startActivity(memory);
                        break;
                    case MainActivity.TEST_LISTENING:
                        Intent listening = new Intent(LearnActivity.this, ListeningTestActivity.class);
                        listening.putExtra(MainActivity.KEY_PASS_VOCABULARIES, vocabularies);
                        startActivity(listening);
                        break;
                    case MainActivity.TEST_WRITING:
                        Intent writing = new Intent(LearnActivity.this, WritingTestActivity.class);
                        writing.putExtra(MainActivity.KEY_PASS_VOCABULARIES, vocabularies);
                        startActivity(writing);
                        break;
                }
            }
        });

        builder.create().show();
    }
    */

}

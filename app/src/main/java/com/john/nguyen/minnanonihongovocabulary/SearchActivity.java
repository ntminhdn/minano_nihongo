package com.john.nguyen.minnanonihongovocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.john.nguyen.minnanonihongovocabulary.adapters.LearnArrayAdapter;
import com.john.nguyen.minnanonihongovocabulary.adapters.SearchArrayAdapter;
import com.john.nguyen.minnanonihongovocabulary.controller.LessonManagement;
import com.john.nguyen.minnanonihongovocabulary.controller.VocabularyOpearation;
import com.john.nguyen.minnanonihongovocabulary.models.RMS;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ListView mLvVocabulary;
    private SearchArrayAdapter mAdapter;
    private ArrayList<Vocabulary> mList;

    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    //--------------------
    private void init(){
        etSearch = (EditText)findViewById(R.id.etSearch);
        mList = VocabularyOpearation.getInstance().search("");
        mAdapter = new SearchArrayAdapter(this, R.layout.item_vocabulary_search, mList, RMS.getInstance().getLanguage());
    }

    private void getWidgets(){
        mLvVocabulary = (ListView)findViewById(R.id.lvVocabulary);
    }

    private void setWidgets(){
        mLvVocabulary.setAdapter(mAdapter);
    }

    private void addWidgetsListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                VocabularyOpearation.getInstance().search(s.toString());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

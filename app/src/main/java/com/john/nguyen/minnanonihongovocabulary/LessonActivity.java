package com.john.nguyen.minnanonihongovocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.john.nguyen.minnanonihongovocabulary.controller.LessonManagement;
public class LessonActivity extends AppCompatActivity {

    public static final String KEY_PASS_LESSON = "lesson";

    ListView mLvLessons;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        init();
        getWidgets();
        setWidgets();
        addWidgetsListener();
    }

    //--------------------
    private void init(){
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LessonManagement.getInstance().getLessonArrayString(getString(R.string.lesson)));
}

    private void getWidgets(){
        mLvLessons = (ListView)findViewById(R.id.lvLesson);
    }

    private void setWidgets(){
        mLvLessons.setAdapter(mAdapter);
    }

    private void addWidgetsListener(){
        mLvLessons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LessonActivity.this, LearnActivity.class);
                intent.putExtra(KEY_PASS_LESSON, i);
                startActivity(intent);
            }
        });
    }
}

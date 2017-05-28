package com.john.nguyen.minnanonihongovocabulary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.john.nguyen.minnanonihongovocabulary.R;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;

import utils.Sound;
import utils.Util;

/**
 * Created by tai.nguyenduc on 3/8/2017.
 */

public class LearnArrayAdapter extends ArrayAdapter<Vocabulary> {

    private Context mContext;
    private int mResource;
    private ArrayList<Vocabulary> mList;
    private String mLanguage;


    public LearnArrayAdapter(Context context, int resource, ArrayList<Vocabulary> vocabularies, String language) {
        super(context, resource, vocabularies);

        mContext = context;
        mResource = resource;
        mList = vocabularies;
        mLanguage = language;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvWord = (TextView)convertView.findViewById(R.id.tvWord);

            viewHolder.tvKanji = (TextView)convertView.findViewById(R.id.tvKanji);
            viewHolder.tvMean = (TextView)convertView.findViewById(R.id.tvMean);
            viewHolder.btnSound = (Button)convertView.findViewById(R.id.btnSound);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final Vocabulary vocabulary = mList.get(position);
        viewHolder.tvWord.setText(vocabulary.getWord());
        viewHolder.tvKanji.setText(mContext.getString(R.string.kanji) + ": " + vocabulary.getKanji());
        viewHolder.tvMean.setText(mContext.getString(R.string.mean) + ": " + vocabulary.getMean(mLanguage));
        viewHolder.btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sound.getInstance().playSoundFromUrl(vocabulary.getSound());
            }
        });
        return convertView;
    }

    private class ViewHolder{
        public TextView tvWord;
        public TextView tvKanji;
        public TextView tvMean;
        private Button btnSound;
    }
}

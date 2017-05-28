package com.john.nguyen.minnanonihongovocabulary.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.john.nguyen.minnanonihongovocabulary.R;
import com.john.nguyen.minnanonihongovocabulary.models.Vocabulary;

import java.util.ArrayList;

/**
 * Created by User on 01/03/2017.
 */

public class VocabularyPracticePagerAdapter extends PagerAdapter {
    Context mContext;
    int mResources;
    ArrayList<Vocabulary> mVocabularys;
    boolean mIsShowWord;
    boolean mIsShowKanji;
    boolean mIsShowMean;

    LayoutInflater mLayoutInflater;

    public VocabularyPracticePagerAdapter(Context context, int resource, ArrayList<Vocabulary> alphabets,
                                        boolean IsShowWord, boolean IsShowKanji, boolean IsShowMean) {
        mContext = context;
        mResources = resource;
        mVocabularys = alphabets;
        mIsShowWord = IsShowWord;
        mIsShowKanji = IsShowKanji;
        mIsShowMean = IsShowMean;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mVocabularys.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View viewContent = mLayoutInflater.inflate(mResources, container, false);

        TextView tvWord = (TextView)viewContent.findViewById(R.id.tvWord);
        TextView tvKanji = (TextView)viewContent.findViewById(R.id.tvKanji);
        TextView tvMean = (TextView)viewContent.findViewById(R.id.tvMean);

        Vocabulary vocabulary = mVocabularys.get(position);
        tvWord.setText(mIsShowWord ? vocabulary.getWord() : "?");
        tvKanji.setText(mIsShowKanji ? vocabulary.getKanji() : "?");
        tvMean.setText(mIsShowMean ? vocabulary.getMean_vi() : "?");

        container.addView(viewContent);
        return viewContent;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

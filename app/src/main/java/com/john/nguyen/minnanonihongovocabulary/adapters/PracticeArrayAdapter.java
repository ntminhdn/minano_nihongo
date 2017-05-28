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
 * Created by tai.nguyenduc on 3/8/2017.
 */

public class PracticeArrayAdapter extends PagerAdapter {

    private Context mContext;
    private int mResource;
    private ArrayList<Vocabulary> mList;
    private boolean mIsShowWord;
    private boolean mIsShowMean;
    private boolean mIsShowKanji;
    private String  mLanguage;

    public PracticeArrayAdapter(Context context, int resource, ArrayList<Vocabulary> vocabularies,
                                boolean isShowWord, boolean isShowMean, boolean isShowKanji, String language){
        mContext = context;
        mResource = resource;
        mList = vocabularies;
        mIsShowWord = isShowWord;
        mIsShowMean = isShowMean;
        mIsShowKanji = isShowKanji;
        mLanguage = language;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View viewContent = inflater.inflate(mResource, container, false);

        TextView tvWord = (TextView)viewContent.findViewById(R.id.tvWord);
        TextView tvMean = (TextView)viewContent.findViewById(R.id.tvMean);
        TextView tvPronunciation = (TextView)viewContent.findViewById(R.id.tvKanji);

        Vocabulary vocabulary = mList.get(position);
        if(mIsShowWord){
            tvWord.setText(vocabulary.getWord());
        }
        else{
            tvWord.setText("???");
        }
        if(mIsShowMean) {
            tvMean.setText(vocabulary.getMean(mLanguage));
        }
        else{
            tvMean.setText("???");
        }
        if(mIsShowKanji) {
            tvPronunciation.setText(vocabulary.getKanji());
        }
        else{
            tvPronunciation.setText("???");
        }
        container.addView(viewContent);
        return viewContent;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
        //super.destroyItem(container, position, object);
    }
}

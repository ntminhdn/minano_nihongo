package com.john.nguyen.minnanonihongovocabulary.models;

import utils.Device;

/**
 * Created by Administrator on 12/02/2017.
 */

public class Vocabulary {
    public static final String FIELD_ID = "id";
    public static final String FIELD_WORD = "word";
    public static final String FIELD_KANJI = "kanji";
    public static final String FIELD_MEAN_EN = "mean_en";
    public static final String FIELD_MEAN_VI = "mean_vi";
    public static final String FIELD_SOUND = "sound";
    public static final String FIELD_LESSON = "lesson";

    private int id;
    private String word;
    private String kanji;
    private String mean_en;
    private String mean_vi;
    private String sound;
    private int lesson;

    public Vocabulary(){
        this.id = -1;
        this.word = "";
        this.kanji = "";
        this.mean_en = "";
        this.mean_vi = "";
        this.sound = "";
        this.lesson = -1;
    }

    public Vocabulary(int id, String word, String kanji, String mean_en, String mean_vi, String sound, int lesson) {
        this.id = id;
        this.word = word;
        this.kanji = kanji;
        this.mean_en = mean_en;
        this.mean_vi = mean_vi;
        this.sound = sound;
        this.lesson = lesson;
    }

    public Vocabulary(String word, String kanji, String mean_en, String mean_vi, String sound, int lesson) {
        this.word = word;
        this.kanji = kanji;
        this.mean_en = mean_en;
        this.mean_vi = mean_vi;
        this.sound = sound;
        this.lesson = lesson;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getMean(String language) {
        if(language.equals(Device.LANGUAGE_EN)){
            return getMean_en();
        }
        else if(language.equals(Device.LANGUAGE_VI)){
            return getMean_vi();
        }
        return getMean_vi();
    }

    public String getMean_en() {
        return mean_en;
    }

    public void setMean_en(String mean_en) {
        this.mean_en = mean_en;
    }

    public String getMean_vi() {
        return mean_vi;
    }

    public void setMean_vi(String mean_vi) {
        this.mean_vi = mean_vi;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", kanji='" + kanji + '\'' +
                ", mean_en='" + mean_en + '\'' +
                ", mean_vi='" + mean_vi + '\'' +
                ", sound='" + sound + '\'' +
                ", lesson='" + lesson + '\'' +
                '}';
    }
}

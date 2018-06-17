package com.example.android.miwok;

/**
 * Created by George on 28/01/2018.
 */

public class Word {
    private String mMiwokTranslation = "";
    private String mDefaultTranslation = "";


    public Word(String defaultTranslation, String miwokTranslation) {
        mMiwokTranslation = miwokTranslation;
        mDefaultTranslation = defaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }
}

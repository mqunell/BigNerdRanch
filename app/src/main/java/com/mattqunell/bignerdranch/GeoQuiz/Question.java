package com.mattqunell.bignerdranch.GeoQuiz;

/*
 * The lack of public/protected/private modifiers makes these methods "package-private" which allows
 * access from package classes, but not subclasses or world
 */

class Question {

    private final int mTextResId;
    private final boolean mAnswerTrue;
    private boolean mAnswered;
    private boolean mCheated;

    Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAnswered = false;
        mCheated = false;
    }

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    boolean isAnswered() {
        return mAnswered;
    }

    void setAnswered() {
        mAnswered = true;
    }

    boolean isCheated() {
        return mCheated;
    }

    void setCheated() {
        mCheated = true;
    }
}

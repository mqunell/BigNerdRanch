package com.mattqunell.bignerdranch;

// The lack of public/protected/private modifiers makes these methods "package-private" which allows
// access from package classes but not subclasses or world

class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mAnswered;

    Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mAnswered = false;
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

    void setAnswered(Boolean answered) {
        mAnswered = answered;
    }
}

package com.mattqunell.bignerdranch;

// The lack of public/protected/private modifiers makes these methods "package-private" which allows
// access from package classes but not subclasses or world

class Question {

    private int mTextResId;
    private boolean mAnswerTrue;

    Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswerTrue;
    }
}

package com.mattqunell.bignerdranch.geo_quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mattqunell.bignerdranch.R;

public class QuizActivity extends AppCompatActivity {

    // Tag for logging, key for bundles, request code for child activity
    private static final String LOG_TAG = "QuizActivity";
    private static final String BUNDLE_KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    // UI elements
    private TextView mQuestionTextview;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;

    // Question array
    private final Question[] mQuestions = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true)
    };

    // Variables for playing through the quiz
    private int mCurrentIndex = 0;
    private int mGuessed = 0;
    private int mScore = 0;
    private final int MAX_CHEATS = 3;
    private int mCheats = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            // mCurrentIndex = KEY_INDEX's value, or 0 if it doesn't have one
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_INDEX, 0);
        }

        // UI elements
        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);

        mCheatButton.setText(getString(R.string.cheat_button, MAX_CHEATS - mCheats, MAX_CHEATS));

        // Set the current question (has to be done after creating elements)
        updateQuestion();
    }

    // Add mCurrentIndex to the savedInstanceState Bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(BUNDLE_KEY_INDEX, mCurrentIndex);
    }

    // Determines whether or not the user cheated
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
         * Currently, the outer if statement is always entered and therefore not actually necessary.
         * It was left in the code for thoroughness and to be built upon.
         */

        // If the user pressed "Cheat!"
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHEAT && data != null) {

            // If the user pressed "Show Answer"
            if (CheatActivity.wasAnswerShown(data)) {
                mQuestions[mCurrentIndex].setCheated();

                // Increment mCheats, update mCheatButton's text, disable mCheatButton if necessary
                mCheats++;
                mCheatButton.setText(getString(R.string.cheat_button, MAX_CHEATS - mCheats,
                        MAX_CHEATS));
                if (mCheats >= MAX_CHEATS) {
                    mCheatButton.setEnabled(false);
                }
            }
            else {
                Toast.makeText(QuizActivity.this, R.string.wise_toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Checks if the user guessed correctly/cheated and if all Questions have been guessed
    public void check_answer_button(View v) {
        // Get the true/false from the button's tag
        boolean userPressedTrue = Boolean.parseBoolean(v.getTag().toString());

        // The String id to be used for the correct/incorrect Toast
        int resultId;

        /*
         * Note: This format does not give the user a point if they cheat, regardless of whether or
         * not they proceeded to choose the correct answer.
         */
        if (mQuestions[mCurrentIndex].isCheated()) {
            resultId = R.string.judgment_toast;
        }
        else {
            // If the user chose the correct answer
            if (userPressedTrue == mQuestions[mCurrentIndex].isAnswerTrue()) {
                resultId = R.string.correct_toast;
                mScore++;
            } else {
                resultId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(QuizActivity.this, resultId, Toast.LENGTH_SHORT).show();

        // Increment mGuessed, set this Question's mAnswered to true, and disable true/false Buttons
        mGuessed++;
        mQuestions[mCurrentIndex].setAnswered();
        setButtonsEnabled(false);

        // Check if all questions have been guessed
        if (mGuessed == mQuestions.length) {
            endGame();
        }
    }

    // "Cheat" functionality
    public void cheat_button(View v) {
        // Start CheatActivity using its encapsulated Intent method
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
        boolean cheatedBefore = mQuestions[mCurrentIndex].isCheated();
        Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue, cheatedBefore);
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
    }

    // "Previous" functionality
    public void previous_button(View v) {
        // Java's % is "remainder", not "mod", so manually wrap around if index = -1
        mCurrentIndex -= 1;
        if (mCurrentIndex == -1) {
            mCurrentIndex = mQuestions.length - 1;
        }

        updateQuestion();
    }

    // "Next" functionality
    public void next_button(View v) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
        updateQuestion();
    }

    // Updates mQuestionTextview, mTrueButton, and mFalseButton for new questions
    private void updateQuestion() {
        Question currentQuestion = mQuestions[mCurrentIndex];

        // Sets mQuestionTextView to the current question
        mQuestionTextview.setText(currentQuestion.getTextResId());

        // Enable buttons if not answered, disable buttons if answered
        if (currentQuestion.isAnswered()) {
            setButtonsEnabled(false);
        }
        else {
            setButtonsEnabled(true);
        }
    }

    // Displays the final score and disables all functionality
    private void endGame() {
        // Update mQuestionTextview with the final results
        String results = "Questions guessed correctly: " + mScore + " / " + mGuessed;
        mQuestionTextview.setText(results);

        // Disable the next/previous Buttons and make mQuestionTextview not clickable
        mNextButton.setEnabled(false);
        mPreviousButton.setEnabled(false);
        mQuestionTextview.setClickable(false);
    }

    // Sets mTrueButton and mFalseButtons' enabled status
    private void setButtonsEnabled(boolean status) {
        mTrueButton.setEnabled(status);
        mFalseButton.setEnabled(status);

        // If all cheats are used, disable mCheatButton
        if (mCheats >= MAX_CHEATS) {
            mCheatButton.setEnabled(false);
        }
        else {
            mCheatButton.setEnabled(status);
        }
    }
}
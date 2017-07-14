package com.mattqunell.bignerdranch;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            // mCurrentIndex = KEY_INDEX's value, or 0 if it doesn't have one
            mCurrentIndex = savedInstanceState.getInt(BUNDLE_KEY_INDEX, 0);
        }

        // V.OCL for elements that have "next" functionality
        View.OnClickListener nextQuestion = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        };

        // TextView: Question
        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        mQuestionTextview.setOnClickListener(nextQuestion);

        // Button: True
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        // Button: False
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        // Button: Cheat
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheat();
            }
        });

        // Button: Previous
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousQuestion();
            }
        });

        // Button: Next
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextQuestion);

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

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHEAT && data != null) {
            if (CheatActivity.wasAnswerShown(data)) {
                mQuestions[mCurrentIndex].setCheated();
            }
            else {
                Toast.makeText(QuizActivity.this, R.string.wise_toast, Toast.LENGTH_SHORT).show();
            }
        }
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

    // Checks if the user cheated/guessed correctly and if all Questions have been guessed
    private void checkAnswer(boolean userPressedTrue) {
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
        mCheatButton.setEnabled(status);
    }

    // "Cheat" functionality
    private void cheat() {
        // Start CheatActivity using its encapsulated Intent method
        boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
        Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
    }

    // "Previous" functionality
    private void previousQuestion() {
        // Java's % is "remainder", not "mod", so manually wrap around if index = -1
        mCurrentIndex -= 1;
        if (mCurrentIndex == -1) {
            mCurrentIndex = mQuestions.length - 1;
        }

        updateQuestion();
    }

    // "Next" functionality
    private void nextQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
        updateQuestion();
    }
}
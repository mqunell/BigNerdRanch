package com.mattqunell.bignerdranch;

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

    // Tag for logging, key for bundles
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    // UI elements
    private TextView mQuestionTextview;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;

    // Question array
    private Question[] mQuestions = new Question[] {
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
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            // mCurrentIndex = KEY_INDEX's value, or 0 if it doesn't have one
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        // V.OCL (from the inner NextQuestion class) to use for "next" functionality
        View.OnClickListener nextQuestion = new NextQuestion();

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
                boolean answerIsTrue = mQuestions[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivity(intent);
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
        Log.v(TAG, "onSaveInstanceState(Bundle)");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    // Updates mQuestionTextview, mTrueButton, and mFalseButton for new questions
    private void updateQuestion() {
        // Sets mQuestionTextView to the current question
        int questionId = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextview.setText(questionId);

        // Enable buttons if not answered, disable buttons if answered
        if (mQuestions[mCurrentIndex].isAnswered()) {
            setButtonsEnabled(false);
        }
        else {
            setButtonsEnabled(true);
        }
    }

    // Checks if the user guessed correctly or not, and if all Questions have been guessed
    private void checkAnswer(boolean userPressedTrue) {
        // The String id to be used for the correct/incorrect Toast
        int resultId;

        if (userPressedTrue == mQuestions[mCurrentIndex].isAnswerTrue()) {
            resultId = R.string.correct_toast;
            mScore++;
        }
        else {
            resultId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, resultId, Toast.LENGTH_SHORT).show();

        // Increment mGuessed, set this Question's mAnswered to true, and disable true/false Buttons
        mGuessed++;
        mQuestions[mCurrentIndex].setAnswered(true);
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

    // Private inner class used for "Next" functionality
    private class NextQuestion implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
            updateQuestion();
        }
    }
}
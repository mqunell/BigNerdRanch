package com.mattqunell.bignerdranch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private TextView mQuestionTextview;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPreviousButton;
    private ImageButton mNextButton;

    private Question[] mQuestions = new Question[] {
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_australia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            // mCurrentIndex = KEY_INDEX's value, or 0 if it doesn't have one
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        View.OnClickListener nextQuestion = new NextQuestion();

        // TextView: Question
        mQuestionTextview = (TextView) findViewById(R.id.question_textview);
        mQuestionTextview.setOnClickListener(nextQuestion);
        updateQuestion();

        // Button: True
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mTrueButton.setEnabled(false);
            }
        });

        // Button: False
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mFalseButton.setEnabled(false);
            }
        });

        // Button: Previous
        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Java's % is "remainder", not "mod", so manually wrap around if index = -1
                mCurrentIndex -= 1;
                if (mCurrentIndex == -1) {
                    mCurrentIndex = mQuestions.length - 1;
                }

                updateQuestion();
                setButtonsEnabled(true);
            }
        });

        // Button: Next
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(nextQuestion);
    }

    // Add mCurrentIndex to the savedInstanceState Bundle
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.v(TAG, "onSaveInstanceState(Bundle)");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    // Checks if the user guessed correctly or not
    private void checkAnswer(boolean userPressedTrue) {
        int resultId;

        if (userPressedTrue == mQuestions[mCurrentIndex].isAnswerTrue()) {
            resultId = R.string.correct_toast;
            setButtonsEnabled(false);
        }
        else {
            resultId = R.string.incorrect_toast;
        }

        Toast.makeText(QuizActivity.this, resultId, Toast.LENGTH_SHORT).show();
    }

    // Sets mQuestionTextView to the current question
    private void updateQuestion() {
        int questionId = mQuestions[mCurrentIndex].getTextResId();
        mQuestionTextview.setText(questionId);
    }

    // Sets mTrueButton and mFalseButtons' enabled status
    private void setButtonsEnabled(boolean status) {
        mTrueButton.setEnabled(status);
        mFalseButton.setEnabled(status);
    }

    // Private inner class used for mQuestionTextview and mNextButton's onClick listeners
    private class NextQuestion implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestions.length;
            updateQuestion();
            setButtonsEnabled(true);
        }
    }
}
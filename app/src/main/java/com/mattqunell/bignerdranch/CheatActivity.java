package com.mattqunell.bignerdranch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // Tags for Logging and Intent extras (in, in, out)
    private static final String LOG_TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.mattqunell.bignerdranch.answer_is_true";
    private static final String EXTRA_ALREADY_SHOWN = "com.mattqunell.bignerdranch.already_shown";
    private static final String EXTRA_ANSWER_SHOWN = "com.mattqunell.bignerdranch.answer_shown";

    // Booleans for storing the Intent extras and whether cheating occurs
    private boolean mAnswerIsTrue;
    private boolean mAlreadyShown;
    private boolean mCheated = false;

    // UI elements
    private TextView mAnswerTextview;
    private Button mShowAnswerButton;
    private TextView mApiTextview;

    // Encapsulates the implementation details of what CheatActivity expects as extras on its Intent
    public static Intent newIntent(Context context, boolean answerIsTrue, boolean alreadyShown) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_ALREADY_SHOWN, alreadyShown);
        return intent;
    }

    // Encapsulates the implementation details of CheatActivity's returned Intent
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        /*
         * getIntent() returns the Intent that started the activity
         * getBooleanExtra(<key name>, <default value if not found>)
         */
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mAlreadyShown = getIntent().getBooleanExtra(EXTRA_ALREADY_SHOWN, false);

        mAnswerTextview = (TextView) findViewById(R.id.answer_textview);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });

        mApiTextview = (TextView) findViewById(R.id.api_level_textview);
        mApiTextview.setText(getString(R.string.api_level, Build.VERSION.SDK_INT));

        // If the answer was already shown
        if (mAlreadyShown) {
            if (mAnswerIsTrue) {
                mAnswerTextview.setText(R.string.true_button);
            }
            else {
                mAnswerTextview.setText(R.string.false_button);
            }

            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
    }

    // Calls setAnswerShownResult when the user goes back, if they did not cheat
    @Override
    public void onBackPressed() {
        if (!mCheated && !mAlreadyShown) {
            setAnswerShownResult(false);
        }

        super.onBackPressed();
    }

    // Set mAnswerTextView to the correct answer
    private void showAnswer() {
        mCheated = true;

        if (mAnswerIsTrue) {
            mAnswerTextview.setText(R.string.true_button);
        }
        else {
            mAnswerTextview.setText(R.string.false_button);
        }

        setAnswerShownResult(true);

        // If the device's version is >= Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Animation that makes mShowAnswerButton invisible
            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        }
        else {
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
    }

    // Send data back to the parent activity
    private void setAnswerShownResult(boolean isAnswerShown) {
        // Create a new Intent with a boolean extra (for whether the answer was shown)
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);

        // Send the Intent back to the parent activity
        setResult(RESULT_OK, data);
    }
}

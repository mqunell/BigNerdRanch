package com.mattqunell.bignerdranch.GeoQuiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.mattqunell.bignerdranch.R;

public class CheatActivity extends AppCompatActivity {

    // Tags for Logging and Intent extras (in, in, out)
    private static final String LOG_TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.mattqunell.bignerdranch.answer_is_true";
    private static final String EXTRA_CHEATED_BEFORE = "com.mattqunell.bignerdranch.cheated_before";
    private static final String EXTRA_CHEATED_NOW = "com.mattqunell.bignerdranch.cheated_now";

    // Booleans for storing the Intent extras and whether cheating occurs
    private boolean mAnswerIsTrue;
    private boolean mCheatedBefore;
    private boolean mCheatedNow = false;

    // UI elements
    private TextView mAnswerTextview;
    private Button mShowAnswerButton;
    private TextView mApiTextview;

    // Encapsulates the implementation details of what CheatActivity expects as extras on its Intent
    public static Intent newIntent(Context context, boolean answerIsTrue, boolean cheatedBefore) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_CHEATED_BEFORE, cheatedBefore);
        return intent;
    }

    // Encapsulates the implementation details of CheatActivity's returned Intent
    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_CHEATED_NOW, false);
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
        mCheatedBefore = getIntent().getBooleanExtra(EXTRA_CHEATED_BEFORE, false);

        // UI elements
        mAnswerTextview = (TextView) findViewById(R.id.answer_textview);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mApiTextview = (TextView) findViewById(R.id.api_level_textview);

        mApiTextview.setText(getString(R.string.api_level, Build.VERSION.SDK_INT));

        // If the user already cheated on this question
        if (mCheatedBefore) {
            displayAnswer();
            hideButton(false);
        }
    }

    // Calls setAnswerShownResult when the user goes back, if they did not cheat
    @Override
    public void onBackPressed() {
        if (!mCheatedBefore && !mCheatedNow) {
            setAnswerShownResult(false);
        }

        super.onBackPressed();
    }

    // Sends data back to the parent activity
    private void setAnswerShownResult(boolean buttonPressed) {
        // Create a new Intent with a boolean extra (for whether "Show Answer" was pressed)
        Intent data = new Intent();
        data.putExtra(EXTRA_CHEATED_NOW, buttonPressed);

        // Send the Intent back to the parent activity
        setResult(RESULT_OK, data);
    }

    // "Show Answer" functionality
    public void show_answer_button(View v) {
        mCheatedNow = true;

        displayAnswer();
        hideButton(true);

        setAnswerShownResult(true);
    }

    // Displays the answer in the TextView
    private void displayAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextview.setText(R.string.true_button);
        }
        else {
            mAnswerTextview.setText(R.string.false_button);
        }
    }

    // Hides the button with/without animation
    private void hideButton(boolean withAnimation) {
        // If the button should be animated and the device's version is >= Lollipop
        if (withAnimation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Animation that makes mShowAnswerButton invisible
            int cx = mShowAnswerButton.getWidth() / 2;
            int cy = mShowAnswerButton.getHeight() / 2;
            float radius = mShowAnswerButton.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswerButton, cx, cy,
                    radius, 0);
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
}

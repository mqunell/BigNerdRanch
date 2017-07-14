package com.mattqunell.bignerdranch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // Tags for Logging and Intent extras (in: E_A_IS_TRUE, out: E_A_SHOWN)
    private static final String LOG_TAG = "CheatActivity";
    private static final String EXTRA_ANSWER_IS_TRUE = "com.mattqunell.bignerdranch.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.mattqunell.bignerdranch.answer_shown";

    // Booleans for whether cheating occurred and storing the Intent extra
    private boolean mCheated = false;
    private boolean mAnswerIsTrue;

    // UI elements
    private TextView mAnswerTextview;
    private Button mShowAnswerButton;

    // Encapsulates the implementation details of what CheatActivity expects as extras on its Intent
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
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

        // getIntent() returns the Intent that started the activity
        // getBooleanExtra(<key name>, <default value if not found>)
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextview = (TextView) findViewById(R.id.answer_textview);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });
    }

    // Calls setAnswerShownResult when the user goes back, if they did not cheat
    @Override
    public void onBackPressed() {
        if (!mCheated) {
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

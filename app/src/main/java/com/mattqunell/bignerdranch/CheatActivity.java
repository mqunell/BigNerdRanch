package com.mattqunell.bignerdranch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // Tag for Intent extras, variable for storing it
    private static final String EXTRA_ANSWER_IS_TRUE = "com.mattqunell.bignerdranch.answer_is_true";
    private boolean mAnswerIsTrue;

    // UI elements
    private TextView mAnswerTextview;
    private Button mShowAnswerButton;

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

    // Encapsulates the implementation details of what CheatActivity expects as extras on its Intent
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    // Set mAnswerTextView to the correct answer
    private void showAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextview.setText(R.string.true_button);
        }
        else {
            mAnswerTextview.setText(R.string.false_button);
        }
    }
}

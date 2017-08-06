package com.mattqunell.bignerdranch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mattqunell.bignerdranch.criminal_intent.CrimeListActivity;
import com.mattqunell.bignerdranch.geo_quiz.QuizActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void quiz_activity_button(View v) {
        startActivity(new Intent(MainActivity.this, QuizActivity.class));
    }

    public void criminal_activity_button(View v) {
        startActivity(new Intent(MainActivity.this, CrimeListActivity.class));
    }
}

package com.mattqunell.bignerdranch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mattqunell.bignerdranch.GeoQuiz.QuizActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button geoQuiz = (Button) findViewById(R.id.main_button_geoquiz);
        geoQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, QuizActivity.class));
            }
        });

        Button criminalIntent = (Button) findViewById(R.id.main_button_criminalintent);
        criminalIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start CriminalIntent's main activity here
            }
        });
    }
}

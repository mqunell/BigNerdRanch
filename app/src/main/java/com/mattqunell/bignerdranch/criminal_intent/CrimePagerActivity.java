package com.mattqunell.bignerdranch.criminal_intent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mattqunell.bignerdranch.R;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    // Tag for Intent extra
    private static final String EXTRA_CRIME_ID = "com.mattqunell.bignerdranch.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    // Encapsulates the implementation details of CrimePagerActivity's returned Intent
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crime_pager);

        // Get the ID of the Crime that was clicked
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // Loop through the Crimes to find the index of the one that was clicked and show it
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    // first_crime_button listener
    public void showFirstCrime(View v) {
        mViewPager.setCurrentItem(0);
    }

    // last_crime_button listener
    public void showLastCrime(View v) {
        mViewPager.setCurrentItem(mCrimes.size());
    }
}

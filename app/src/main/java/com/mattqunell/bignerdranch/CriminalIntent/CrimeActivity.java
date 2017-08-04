package com.mattqunell.bignerdranch.CriminalIntent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/*
 * CrimeActivity extends the generic and abstract SingleFragmentActivity, and only needs a method
 * that returns a new CrimeFragment.
 */
public class CrimeActivity extends SingleFragmentActivity {

    // Tag for Intent extra
    public static final String EXTRA_CRIME_ID = "com.mattqunell.bignerdranch.crime_id";

    // Encapsulates the implementation details of CrimeActivity's returned Intent
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}

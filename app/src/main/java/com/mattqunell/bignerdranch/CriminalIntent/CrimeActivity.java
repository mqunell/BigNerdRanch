package com.mattqunell.bignerdranch.CriminalIntent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/*
 * CrimeActivity extends the generic and abstract SingleFragmentActivity. It has an encapsulated
 * Intent method that allows other Activities/Fragments to easily create an Intent with the specific
 * Crime UUID as an extra. createFragment() requires knowledge of CrimeFragment, but allows it to
 * remain independent by not needing to access this activity for an Intent extra.
 */
public class CrimeActivity extends SingleFragmentActivity {

    // Tag for Intent extra
    private static final String EXTRA_CRIME_ID = "com.mattqunell.bignerdranch.crime_id";

    // Encapsulates the implementation details of CrimeActivity's returned Intent
    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    // Creates an instance of CrimeFragment from its encapsulated method
    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
}

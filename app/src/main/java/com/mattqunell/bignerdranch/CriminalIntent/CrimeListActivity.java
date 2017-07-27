package com.mattqunell.bignerdranch.CriminalIntent;

import android.support.v4.app.Fragment;

/*
 * CrimeListActivity extends the generic and abstract SingleFragmentActivity, and only needs a
 * method that returns a new CrimeListFragment.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}

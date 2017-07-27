package com.mattqunell.bignerdranch.CriminalIntent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mattqunell.bignerdranch.R;

/*
 * CrimeActivity extends the generic and abstract SingleFragmentActivity, and only needs a method
 * that returns a new CrimeFragment.
 */
public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}

package com.mattqunell.bignerdranch.criminal_intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.mattqunell.bignerdranch.R;

/*
 * Nearly every Activity in the BNR book will require this same code, so it is stashed in an
 * abstract class for reuse. Subclasses (ex. CrimeListActivity) must implement a method that returns
 * an instance of the Fragment it hosts.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    // Abstract method that must be implemented in child classes to return a Fragment
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {

            // Get the Fragment from the child class
            fragment = createFragment();

            /*
             * Transactions add, remove, attach, detach, and replace fragments. This creates a new
             * fragment transaction, includes one operation, and commits it. Adding the Fragment to
             * the FragmentManager calls its onAttach, onCreate, and onCreateView methods.
             */
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}

package com.mattqunell.bignerdranch.CriminalIntent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    // Static getter that creates sCrimeLab if it doesn't exist and returns it
    public static CrimeLab get() {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }

        return sCrimeLab;
    }

    // Private Constructor to limit instantiation
    private CrimeLab() {
        mCrimes = new ArrayList<>();

        // Dummy Crimes (temporary)
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0); // Every other Crime
            mCrimes.add(crime);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }
}

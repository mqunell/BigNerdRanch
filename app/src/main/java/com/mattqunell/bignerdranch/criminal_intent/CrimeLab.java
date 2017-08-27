package com.mattqunell.bignerdranch.criminal_intent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
 * CrimeLab is implemented as a singleton. A singleton is a class that allows only one instance of
 * itself to be created. It exists as long as the application is in memory, and is available through
 * lifecycle changes in activities and fragments. Singletons allow data to be easily passed between
 * controller classes, but should not be used for everything or as long-term storage solutions.
 */
class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    // Static getter that creates sCrimeLab if it doesn't exist and returns it
    static CrimeLab get() {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab();
        }

        return sCrimeLab;
    }

    // Private Constructor to limit instantiation
    private CrimeLab() {
        mCrimes = new ArrayList<>();
    }

    void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    void removeCrime(Crime crime) {
        mCrimes.remove(crime);
    }

    Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }

    List<Crime> getCrimes() {
        return mCrimes;
    }
}

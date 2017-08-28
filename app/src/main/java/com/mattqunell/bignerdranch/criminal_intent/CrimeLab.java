package com.mattqunell.bignerdranch.criminal_intent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mattqunell.bignerdranch.criminal_intent.database.CrimeBaseHelper;
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema;
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema.CrimeTable;

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

    private SQLiteDatabase mDatabase;

    // Static getter that creates sCrimeLab if it doesn't exist and returns it
    static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }

    // Private Constructor to limit instantiation
    private CrimeLab(Context context) {
        mDatabase = new CrimeBaseHelper(context.getApplicationContext()).getWritableDatabase();

        /*mCrimes = new ArrayList<>();*/
    }

    void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    // Edits an existing Crime
    void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,
                values,
                CrimeTable.Cols.UUID + " = ?", // Prevents SQL injection
                new String[] { uuidString });
    }

    void removeCrime(Crime crime) {
        /*mCrimes.remove(crime);*/
    }

    Crime getCrime(UUID id) {
        /*for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }*/

        return null;
    }

    List<Crime> getCrimes() {
        /*return mCrimes;*/
        return null;
    }

    // Helper method that essentially converts a Crime into a ContentValues
    private static ContentValues getContentValues(Crime crime) {

        // ContentValues is a key-value class specifically designed for SQLite data
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);

        return values;
    }
}

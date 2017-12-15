package com.mattqunell.bignerdranch.criminal_intent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mattqunell.bignerdranch.criminal_intent.database.CrimeBaseHelper;
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeCursorWrapper;
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema.CrimeTable;

import java.io.File;
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

    private Context mContext;
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
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    // Adds a Crime to the database
    void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    // Edits an existing Crime in the database
    void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME,
                values,
                CrimeTable.Cols.UUID + " = ?", // Prevents SQL injection
                new String[] { uuidString });
    }

    // Removes a Crime from the database
    void removeCrime(Crime crime) {
        String uuidString = crime.getId().toString();

        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    // Gets a Crime from the database
    Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }

    // Gets an ArrayList of all Crimes in the database
    List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return crimes;
    }

    public File getPhotoFile(Crime crime) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }

    // Helper method that essentially converts a Crime into a ContentValues
    private static ContentValues getContentValues(Crime crime) {

        // ContentValues is a key-value class specifically designed for SQLite data
        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID,    crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,   crime.getTitle());
        values.put(CrimeTable.Cols.DATE,    crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,  crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }

    // Helper method that searches for a Crime (?)
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {

        // Args: table, columns, where, whereArgs, groupBy, having, orderBy
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // null selects all
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }
}

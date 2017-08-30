package com.mattqunell.bignerdranch.criminal_intent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mattqunell.bignerdranch.criminal_intent.Crime;
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

/*
 * CrimeCursorWrapper is used to traverse the database through CursorWrapper's functionality. The
 * getCrime() method parses and returns the Crime at CrimeCursorWrapper's location in the database.
 */
public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // Parses a Crime from the database
    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }
}

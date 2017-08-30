package com.mattqunell.bignerdranch.criminal_intent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema.CrimeTable;

/*
 * When an instance of CrimeBaseHelper is created, the super(...) call in the constructor does up to
 * three things, as necessary:
 *     1. Create the database and set the version number (onCreate(...))
 *     2. Upgrade the database (onUpgrade(...))
 *     3. Open the database
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Called automatically when a database needs to be created
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Crime table
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID   + ", " +
                CrimeTable.Cols.TITLE  + ", " +
                CrimeTable.Cols.DATE   + ", " +
                CrimeTable.Cols.SOLVED +
                ")"
        );
    }

    // Called automatically when a database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Intentionally left blank
    }
}

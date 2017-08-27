package com.mattqunell.bignerdranch.criminal_intent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Import the inner CrimeTable class so it can be called directly, without "CrimeDbSchema."
import com.mattqunell.bignerdranch.criminal_intent.database.CrimeDbSchema.CrimeTable;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    /*
     * When a new CrimeBaseHelper is created, the super call creates the database if necessary, and
     * opens it. If the database gets created, onCreate(SQLiteDatabase) is called, which saves the
     * latest version number. If the database already exists but is an older version, onUpgrade(...)
     * is called, which upgrades it.
     */
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

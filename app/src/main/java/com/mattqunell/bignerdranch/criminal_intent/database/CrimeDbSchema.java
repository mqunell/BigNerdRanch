package com.mattqunell.bignerdranch.criminal_intent.database;

/*
 * CrimeDbSchema contains an inner class that sets the names of the table and columns. Importing
 * CrimeTable allows it to be accessed directly, without calling "CrimeDbScheme." first.
 */
public class CrimeDbSchema {

    // Inner class
    public static final class CrimeTable {

        // Table name
        public static final String NAME = "crimes";

        // Column names
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}

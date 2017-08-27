package com.mattqunell.bignerdranch.criminal_intent.database;

public class CrimeDbSchema {

    // Inner class used for calling CrimeTable.NAME, CrimeTablet.Cols.TITLE, etc
    public static final class CrimeTable {
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}

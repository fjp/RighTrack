/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.fjp.rightrack.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import at.fjp.rightrack.Database.RighTrackContract.PriorityEntry;
import at.fjp.rightrack.Database.RighTrackContract.RecurrenceEntry;
import at.fjp.rightrack.TodoFragment;

/**
 * Manages a local database for rightrack data.
 */
public class RighTrackDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = TodoFragment.class.getSimpleName() + " BUG ";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 6;

    static final String DATABASE_NAME = "rightrack.db";

    public RighTrackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v(LOG_TAG, "RighTrackDbHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold priority.  A priority consists of the string supplied in the
        // priority level
        final String SQL_CREATE_PRIORITY_TABLE = "CREATE TABLE " + PriorityEntry.TABLE_NAME + " (" +
                PriorityEntry._ID + " INTEGER PRIMARY KEY," +
                PriorityEntry.COLUMN_PRIORITY_LEVEL + " TEXT UNIQUE NOT NULL" +
                ");";

        // Create a table to hold recurrence.  A recurrence consists of the string supplied in the
        // recurrence
        final String SQL_CREATE_RECURRENCE_TABLE = "CREATE TABLE " + RecurrenceEntry.TABLE_NAME + " (" +
                RecurrenceEntry._ID + " INTEGER PRIMARY KEY," +
                RecurrenceEntry.COLUMN_RECURRENCE + " TEXT UNIQUE NOT NULL" +
                ");";

        Log.v(LOG_TAG, "onCreate" + "SQL CREATE RECURRENCE TABLE");


        final String SQL_CREATE_TODO_TABLE = "CREATE TABLE " + RighTrackContract.TodoEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                RighTrackContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                RighTrackContract.TodoEntry.COLUMN_PRIO_KEY + " INTEGER NOT NULL, " +
                RighTrackContract.TodoEntry.COLUMN_REC_KEY + " INTEGER NOT NULL, " +
                RighTrackContract.TodoEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                RighTrackContract.TodoEntry.COLUMN_DUE_DATE + " INTEGER, " +
                RighTrackContract.TodoEntry.COLUMN_TODO + " TEXT NOT NULL, " +

                // Set up the priority column as a foreign key to priority table.
                " FOREIGN KEY (" + RighTrackContract.TodoEntry.COLUMN_PRIO_KEY + ") REFERENCES " +
                RighTrackContract.TodoEntry.TABLE_NAME + " (" + RighTrackContract.TodoEntry._ID + "), " +

                // Set up the recurrence column as a foreign key to recurrence table.
                " FOREIGN KEY (" + RighTrackContract.TodoEntry.COLUMN_REC_KEY + ") REFERENCES " +
                RighTrackContract.TodoEntry.TABLE_NAME + " (" + RighTrackContract.TodoEntry._ID + ") );";

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                //" UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ", " +
                //WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_PRIORITY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RECURRENCE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over TODO böses ding: USE ALTER TABLE
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RighTrackContract.PriorityEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RighTrackContract.RecurrenceEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RighTrackContract.TodoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class RighTrackProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RighTrackDbHelper mOpenHelper;

    static final int TODO = 100;
    static final int PRIORITY = 200;
    static final int RECURRENCE = 300;

    private static final SQLiteQueryBuilder sTodoQueryBuilder;

    static{
        sTodoQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //todo
        // INNER JOIN
        //  priority
        // ON
        //  todo.priority = priority._id
        // INNER JOIN
        //  recurrence
        // ON
        //  todo.recurrence = recurrence._id
        sTodoQueryBuilder.setTables(
                RighTrackContract.TodoEntry.TABLE_NAME + " INNER JOIN " +
                        RighTrackContract.PriorityEntry.TABLE_NAME +
                        " ON " + RighTrackContract.TodoEntry.TABLE_NAME +
                        "." + RighTrackContract.TodoEntry.COLUMN_PRIO_KEY +
                        " = " + RighTrackContract.PriorityEntry.TABLE_NAME +
                        "." + RighTrackContract.PriorityEntry._ID  + " INNER JOIN " +
                        RighTrackContract.RecurrenceEntry.TABLE_NAME +
                        " ON " + RighTrackContract.TodoEntry.TABLE_NAME +
                        "." + RighTrackContract.TodoEntry.COLUMN_REC_KEY +
                        " = " + RighTrackContract.RecurrenceEntry.TABLE_NAME +
                        "." + RighTrackContract.RecurrenceEntry._ID);
    }

    private Cursor getTodo(Uri uri, String[] projection, String sortOrder) {
        String locationSetting = RighTrackContract.TodoEntry.getPriorityLevelFromUri(uri);
        long startDate = RighTrackContract.TodoEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;


        return sTodoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null, // selection
                null, // selectionArgs
                null,
                null,
                sortOrder
        );
    }

    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // I know what you're thinking.  Why create a UriMatcher when you can use regular
        // expressions instead?  Because you're not crazy, that's why.

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RighTrackContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, RighTrackContract.PATH_TODO, TODO);
        //matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*", WEATHER_WITH_LOCATION);
        //matcher.addURI(authority, WeatherContract.PATH_WEATHER + "/*/#", WEATHER_WITH_LOCATION_AND_DATE);

        matcher.addURI(authority, RighTrackContract.PATH_PRIORITY, PRIORITY);

        matcher.addURI(authority, RighTrackContract.PATH_RECURRENCE, RECURRENCE);
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new RighTrackDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.
     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case TODO:
                return RighTrackContract.TodoEntry.CONTENT_TYPE;
            case PRIORITY:
                return RighTrackContract.PriorityEntry.CONTENT_TYPE;
            case RECURRENCE:
                return RighTrackContract.RecurrenceEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "todo"
            case TODO: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RighTrackContract.TodoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "priority"
            case PRIORITY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RighTrackContract.PriorityEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "recurrence"
            case RECURRENCE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RighTrackContract.RecurrenceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TODO: {
                normalizeDate(values);
                long _id = db.insert(RighTrackContract.TodoEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = RighTrackContract.TodoEntry.buildTodoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case PRIORITY: {
                long _id = db.insert(RighTrackContract.PriorityEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = RighTrackContract.PriorityEntry.buildPriorityUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case RECURRENCE: {
                long _id = db.insert(RighTrackContract.RecurrenceEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = RighTrackContract.RecurrenceEntry.buildRecurrenceUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case TODO:
                rowsDeleted = db.delete(
                        RighTrackContract.TodoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRIORITY:
                rowsDeleted = db.delete(
                        RighTrackContract.PriorityEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case RECURRENCE:
                rowsDeleted = db.delete(
                        RighTrackContract.RecurrenceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(RighTrackContract.TodoEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(RighTrackContract.TodoEntry.COLUMN_DATE);
            values.put(RighTrackContract.TodoEntry.COLUMN_DATE, RighTrackContract.normalizeDate(dateValue));
        }
    }

    // todo normalize due date value like above with date ^

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case TODO:
                normalizeDate(values);
                rowsUpdated = db.update(RighTrackContract.TodoEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case PRIORITY:
                rowsUpdated = db.update(RighTrackContract.PriorityEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case RECURRENCE:
                rowsUpdated = db.update(RighTrackContract.RecurrenceEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
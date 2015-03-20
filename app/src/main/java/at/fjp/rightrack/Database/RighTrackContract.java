package at.fjp.rightrack.Database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the rightrack database.
 */
public class RighTrackContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "at.fjp.rightrack.app";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://at.fjp.rightrack.app/priority/ is a valid path for
    // looking at priority data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_TODO = "todo";
    public static final String PATH_PRIORITY = "priority";
    public static final String PATH_RECURRENCE = "recurrence";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the table contents of the priority table
     */
    public static final class PriorityEntry implements BaseColumns {
        public static final String TABLE_NAME = "priority";

        public static final String COLUMN_PRIORITY_LEVEL = "priority_level";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PRIORITY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRIORITY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRIORITY;

        public static Uri buildPriorityUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /*
        Inner class that defines the table contents of the recurrence table
     */
    public static final class RecurrenceEntry implements BaseColumns {
        public static final String TABLE_NAME = "recurrence";

        public static final String COLUMN_RECURRENCE = "recurrence";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECURRENCE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECURRENCE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECURRENCE;

        public static Uri buildRecurrenceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the todo table */
    public static final class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todo";

        // Column with the foreign key into the priority table.
        public static final String COLUMN_PRIO_KEY = "priority_id";

        // Column with the foreign key into the recurrence table.
        public static final String COLUMN_REC_KEY = "recurrence_id";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "date";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DUE_DATE = "due_date";

        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_TODO = "todo";



        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TODO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TODO;


        public static Uri buildTodoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        /*
            Student: Fill in this buildTodoPriority function
         */
        public static Uri buildTodoPriority(String priorityLevel) {
            return null;
        }

        public static Uri buildTodoPriorityWithStartDate(
                String priority, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(priority)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
        }

        public static Uri buildTodoRecurrenceWithStartDate(
                String recurrence, long startDate) {
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(recurrence)
                    .appendQueryParameter(COLUMN_DATE, Long.toString(normalizedDate)).build();
        }

        public static Uri buildTodoPriorityWithDate(String priority, long date) {
            return CONTENT_URI.buildUpon().appendPath(priority)
                    .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static Uri buildTodoRecurrenceWithDate(String recurrence, long date) {
            return CONTENT_URI.buildUpon().appendPath(recurrence)
                    .appendPath(Long.toString(normalizeDate(date))).build();
        }

        public static String getPriorityLevelFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getRecurrenceFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static long getDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(3));
        }

        public static long getStartDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }

        public static long getDueDateFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(4));
        }

        public static long getStartDueDateFromUri(Uri uri) {
            String dateString = uri.getQueryParameter(COLUMN_DUE_DATE);
            if (null != dateString && dateString.length() > 0)
                return Long.parseLong(dateString);
            else
                return 0;
        }
    }
}


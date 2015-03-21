package at.fjp.rightrack;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.fjp.rightrack.Database.RighTrackContract;

/**
 * Created by fjp on 21.03.15.
 */
public class TodoData {

    private static Context mContext = null;

    TodoData(Context context) {

        mContext = context;

        addData();
    }

    private void addData() {

        long lowPriorityId = addPriority("LOW");
        long middlePriorityId = addPriority("MIDDLE");
        long highPriorityId = addPriority("HIGH");

        long dailyRecurrenceId = addRecurrence("DAILY");
        long monthlyRecurrenceId = addRecurrence("MONTHLY");
        long yearlyRecurrenceId = addRecurrence("YEARLY");
        long Id = addRecurrence("OTHER");


        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.

        Time dayTime = new Time();
        dayTime.setToNow();

        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        // now we work exclusively in UTC
        dayTime = new Time();


        // Cheating to convert this to UTC time, which is what we want anyhow
        long dateTime = dayTime.setJulianDay(julianStartDay);

        // Predefined Todos
        addTodo("min 3l Trinken", highPriorityId, dailyRecurrenceId, dateTime, dateTime);


        // Create some dummy data for the ListView.  Here's a sample weekly forecast
//        String[] data = {
//                "Stop Thinking Too Much",
//                "Get a Job - BMW, Uni, ... Werkstudent oder Praktikum!",
//                "Eat an Apple",
//                "Komplimente geben/denken",
//                "Fri 6/27 - Foggy - 21/10",
//                "Kurzum: Wer nie fragt, kann auch nie etwas bekommen. Wer aber wagt, gewinnt gleich dreifach. Mindestens an Erfahrung.\n" +
//                        "\n" +
//                        "Fragen kostet nichts – außer Überwindung! Geh unter die kalte Dusche!",
//                "Lass die Gedanken - ausnahmslos ALLE! - das sein was sie sind! Gedanken. Lass sie kommen und gehen und grübel nicht sinnlos darüber! \n" +
//                        "Gib die Analyse, der Meinungen anderer über dich, auf! Haters gonna hate.",
//                "Fail. A Lot.\n" +
//                        "Be Polarizing (Gegensätztlich) - Be Yourself\n" +
//                        "Stop Giving A FUCK!\n" +
//                        "Do One Thing That Scares You Everyday!\n" +
//                        "GET IN TOUCH WITH PEOPLE\n" +
//                        "it developes your brain!\n" +
//                        "1) Make peace with your past\n" +
//                        "so it won't disturb your present.\n" +
//                        "2) What other people think of you\n" +
//                        "is none of your business.\n" +
//                        "3) Time heals almost everything.\n" +
//                        "Give it time.\n" +
//                        "4) No one is in charge\n" +
//                        "of your happiness. Except you.\n" +
//                        "5) Don't compare your life to others\n" +
//                        "and don't judge them, you have no idea what their jorney is all about.\n" +
//                        "6) Stop thinking too much.\n" +
//                        "Its alright not to know the answers.\n" +
//                        "They will come to you when you least expect it.\n" +
//                        "7) Smile.\n" +
//                        "You don't own all the problems in the world.\n" +
//                        "---------\n" +
//                        "8) Just start something!"
//
//        };
    }

    long addTodo(String todo, long priorityId, long recurrenceId, long date, long dueDate) {
        long todoId;

        // First, check if the priority name exists in the db
        Cursor todoCursor = mContext.getContentResolver().query(
                RighTrackContract.TodoEntry.CONTENT_URI,
                new String[]{RighTrackContract.TodoEntry._ID},
                RighTrackContract.TodoEntry.COLUMN_TODO + " = ?",
                new String[]{todo},
                null);

        if (todoCursor.moveToFirst()) {
            int todoIdIndex = todoCursor.getColumnIndex(RighTrackContract.TodoEntry._ID);
            todoId = todoCursor.getLong(todoIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues todoValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            todoValues.put(RighTrackContract.TodoEntry.COLUMN_TODO, todo);
            todoValues.put(RighTrackContract.TodoEntry.COLUMN_PRIO_KEY, priorityId);
            todoValues.put(RighTrackContract.TodoEntry.COLUMN_REC_KEY, recurrenceId);
            todoValues.put(RighTrackContract.TodoEntry.COLUMN_DATE, date);
            todoValues.put(RighTrackContract.TodoEntry.COLUMN_DUE_DATE, dueDate);

            // Finally, insert priority data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    RighTrackContract.TodoEntry.CONTENT_URI,
                    todoValues
            );

            // The resulting URI contains the ID for the row.  Extract the priorityId from the Uri.
            todoId = ContentUris.parseId(insertedUri);
        }

        todoCursor.close();
        // Wait, that worked?  Yes!
        return todoId;
    }

    long addPriority(String priority) {
        long priorityId;

        // First, check if the location with this city name exists in the db
        Cursor priorityCursor = mContext.getContentResolver().query(
                RighTrackContract.PriorityEntry.CONTENT_URI,
                new String[]{RighTrackContract.PriorityEntry._ID},
                RighTrackContract.PriorityEntry.COLUMN_PRIORITY_LEVEL + " = ?",
                new String[]{priority},
                null);

        if (priorityCursor.moveToFirst()) {
            int priorityIdIndex = priorityCursor.getColumnIndex(RighTrackContract.PriorityEntry._ID);
            priorityId = priorityCursor.getLong(priorityIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues priorityValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            priorityValues.put(RighTrackContract.PriorityEntry.COLUMN_PRIORITY_LEVEL, priority);

            // Finally, insert priority data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    RighTrackContract.PriorityEntry.CONTENT_URI,
                    priorityValues
            );

            // The resulting URI contains the ID for the row.  Extract the priorityId from the Uri.
            priorityId = ContentUris.parseId(insertedUri);
        }

        priorityCursor.close();
        // Wait, that worked?  Yes!
        return priorityId;
    }

    long addRecurrence(String recurrences) {
        long recurrenceId;

        // First, check if the recurrence with this city name exists in the db
        Cursor recurrenceCursor = mContext.getContentResolver().query(
                RighTrackContract.RecurrenceEntry.CONTENT_URI,
                new String[]{RighTrackContract.RecurrenceEntry._ID},
                RighTrackContract.RecurrenceEntry.COLUMN_RECURRENCE + " = ?",
                new String[]{recurrences},
                null);

        if (recurrenceCursor.moveToFirst()) {
            int recurrenceIdIndex = recurrenceCursor.getColumnIndex(RighTrackContract.RecurrenceEntry._ID);
            recurrenceId = recurrenceCursor.getLong(recurrenceIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues recurrenceValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            recurrenceValues.put(RighTrackContract.RecurrenceEntry.COLUMN_RECURRENCE, recurrences);

            // Finally, insert priority data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    RighTrackContract.RecurrenceEntry.CONTENT_URI,
                    recurrenceValues
            );

            // The resulting URI contains the ID for the row.  Extract the priorityId from the Uri.
            recurrenceId = ContentUris.parseId(insertedUri);
        }

        recurrenceCursor.close();
        // Wait, that worked?  Yes!
        return recurrenceId;
    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }
}

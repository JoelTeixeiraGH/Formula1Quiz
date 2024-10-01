package pt.ipp.estg.formula1q.utils;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.Toast;

import pt.ipp.estg.formula1q.models.Race;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class RaceCalendar {
    private static final int REMINDER_MINUTES = 30;
    private static final long RACE_DURATION_MS = 90*60*1000;

    public static void addEvents(Context context, List<Race> raceList){
        removeEvents(context, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                raceList.forEach(race -> {
                    addEvent(context, race);
                });
            }
        }).start();
        Toast.makeText(context, "Events added", Toast.LENGTH_SHORT).show();
    }

    private static void addEvent(Context context, Race race) {
        long raceStartTimeMs = parseRaceDate(race);

        if(raceStartTimeMs == -1) {
            Log.e("RaceCalendar",
                    "Failed to add race event for " + race.getRaceName());
            return;
        }

        ContentResolver contentResolver = context.getContentResolver();

        String timeZone = TimeZone.getDefault().getID();
        ContentValues eventValues = new ContentValues();
        eventValues.put(CalendarContract.Events.DTSTART, raceStartTimeMs);
        eventValues.put(CalendarContract.Events.DTEND, raceStartTimeMs + RACE_DURATION_MS);
        eventValues.put(CalendarContract.Events.TITLE, race.getRaceName());
        eventValues.put(CalendarContract.Events.DESCRIPTION, "Formula1Q");
        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);
        String eventId = contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues)
                .getLastPathSegment();

        ContentValues reminderValues = new ContentValues();
        reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminderValues.put(CalendarContract.Reminders.MINUTES, REMINDER_MINUTES);
        contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues);
    }

    /**
     *
     * @param race
     * @return Race start time in milliseconds
     */
    public static long parseRaceDate(Race race) {
        String formatedRaceDate = getFormatedRaceDate(race.getDate(), race.getTime());
        return parseRaceDate(formatedRaceDate);
    }

    public static long parseRaceDate(String preFormatedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
        try {
            return dateFormat.parse(preFormatedDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getFormatedRaceDate(String raceDate, String raceTime){
        raceTime = raceTime == null ? "00:00:00Z" : raceTime;
        return raceDate + " " + raceTime;
    }

    public static void removeEvents(Context context, boolean showToast){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = context.getContentResolver();

                Cursor cursor = contentResolver.query(CalendarContract.Events.CONTENT_URI,
                        new String[]{CalendarContract.Events._ID},
                        String.format("%s = 'Formula1Q'", CalendarContract.Events.DESCRIPTION),
                        null,
                        null);

                contentResolver.delete(CalendarContract.Events.CONTENT_URI,
                        String.format("%s = 'Formula1Q'", CalendarContract.Events.DESCRIPTION),
                        null);

                if(cursor.moveToFirst()){
                    do{
                        long eventId = cursor.getLong(0);
                        contentResolver.delete(CalendarContract.Reminders.CONTENT_URI,
                                String.format("%s = $d", CalendarContract.Reminders.EVENT_ID, eventId),
                                null);
                    } while(cursor.moveToNext());
                }
            }
        })
                .start();
        if(showToast) {
            Toast.makeText(context, "Events removed", Toast.LENGTH_SHORT).show();
        }
    }
}

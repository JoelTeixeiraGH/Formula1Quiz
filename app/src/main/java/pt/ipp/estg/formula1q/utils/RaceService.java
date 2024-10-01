package pt.ipp.estg.formula1q.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import pt.ipp.estg.formula1q.api.APIClient;

public class RaceService extends Service {

    private static final long INTERVAL_MINUTES = 18;
    private static long testingInitialDelay = 10*1000;


    @Override
    public void onCreate() {
        super.onCreate();
        APIClient.getScheduledRacesByYear(LocalDate.now().getYear(), result -> {
            result.getRaces().forEach(race -> {
                String formattedRaceDate = RaceCalendar.getFormatedRaceDate(race.getDate(), race.getTime());
                long raceStartMs = RaceCalendar.parseRaceDate(formattedRaceDate);
                long initialDelay = raceStartMs - Calendar.getInstance().getTimeInMillis();

                Data data = new Data.Builder()
                        .putString("eventFormattedDate", formattedRaceDate)
                        .putString("eventName", race.getRaceName())
                        .build();

                PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(ScheduledWorker.class, INTERVAL_MINUTES, TimeUnit.MINUTES)
                        .setInputData(data)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .addTag(formattedRaceDate)
                        .build();

                testingInitialDelay+=10*1000;
                WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
            });
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

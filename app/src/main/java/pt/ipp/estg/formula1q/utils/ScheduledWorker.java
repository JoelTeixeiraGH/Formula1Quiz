package pt.ipp.estg.formula1q.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;

public class ScheduledWorker extends Worker {
    private final long RACE_DURATION = 90*60*1000;
    private String eventName, eventFormattedDate;
    private Context mContext;

    public ScheduledWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        eventName = workerParams.getInputData().getString("eventName");
        eventFormattedDate = workerParams.getInputData().getString("eventFormattedDate");
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        long raceStartMs = RaceCalendar.parseRaceDate(eventFormattedDate);
        long raceEndMs = raceStartMs + RACE_DURATION;

        if(Calendar.getInstance().getTimeInMillis() > raceEndMs){
            WorkManager.getInstance(getApplicationContext()).cancelAllWorkByTag(eventFormattedDate);
        }
        Notification.send(mContext,
                "New Question for " + eventName,
                "Click to answer!",
                eventName,
                eventFormattedDate);

        return Result.success();
    }
}

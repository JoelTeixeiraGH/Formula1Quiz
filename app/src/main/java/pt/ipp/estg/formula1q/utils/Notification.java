package pt.ipp.estg.formula1q.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.lang.annotation.IncompleteAnnotationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import pt.ipp.estg.formula1q.activities.MainActivity;
import pt.ipp.estg.formula1q.activities.QuestionActivity;
import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.models.Race;

public class Notification {
    private static final String CHANNEL_ID = "F1Q";
    private static boolean channelCreated = false;

    public static void send(Context context, String title, String content, String eventName,
                            @NonNull String eventDate){

        createNotificationChannel(context);

        Intent mainActivityIntent =
                new Intent(context, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent questionActivityIntent = new Intent(context, QuestionActivity.class);

        questionActivityIntent.putExtra("isSpecialQuestion", true);
        questionActivityIntent.putExtra("eventName", eventName);
        questionActivityIntent.putExtra("eventDate", eventDate);

        PendingIntent notificationIntent = PendingIntent.getActivities(context, 0,
                new Intent[] {mainActivityIntent, questionActivityIntent}, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fonelogo)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(notificationIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(context)
                .notify(1, notificationBuilder.build());
    }

    private static void createNotificationChannel(Context context){
        if(!channelCreated && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "New Challenge",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            context.getSystemService(NotificationManager.class)
                    .createNotificationChannel(channel);
            channelCreated = true;
        }
    }
}
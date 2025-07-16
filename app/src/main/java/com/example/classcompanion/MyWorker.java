package com.example.classcompanion;

import static com.example.classcompanion.R.*;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("ReminderWorker", "Work executed!");
        showNotification("Class Companion","Don't forget to check today's schedule!");

        return Result.success();
    }

    private void showNotification(String title,String message) {
        NotificationManager noti = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String ChannelId = "reminder_channel";
        String ChannelName = "Daily Reminder";

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ChannelId,ChannelName,NotificationManager.IMPORTANCE_DEFAULT);
            noti.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),ChannelId)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        drawable.bell
                ))
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(drawable.highlight)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        noti.notify(1,builder.build());

    }
}

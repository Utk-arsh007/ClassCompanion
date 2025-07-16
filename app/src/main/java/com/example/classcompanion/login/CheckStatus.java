package com.example.classcompanion.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.classcompanion.Home;
import com.example.classcompanion.MyWorker;
import com.example.classcompanion.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CheckStatus extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_status);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(this, SignUp.class));
            finish();
        }
        else{
            scheduleDailyReminder();
            startActivity(new Intent(CheckStatus.this, Home.class));
            finish();
        }


    }

    private void scheduleDailyReminder() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        long scheduleTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        if (scheduleTime<currentTime){
            scheduleTime += TimeUnit.DAYS.toMillis(1);
        }

        long initialDelay = scheduleTime-currentTime;

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(MyWorker.class,24,TimeUnit.HOURS)
                .setInitialDelay(initialDelay,TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("daily_reminder", ExistingPeriodicWorkPolicy.KEEP,workRequest);
    }




}
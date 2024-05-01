package com.example.smsreader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;


import java.util.Timer;
import java.util.TimerTask;

public class ExampleService extends Service {
    public int counter=0;

    public void onCreate() {
        super.onCreate();
       // startMyOwnForeground();
       // startForeground(1,new Notification());
    }

    public void startMyOwnForeground(String string) {

        Uri ringtone= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        String input=string;
        Intent notification_intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,notification_intent,0);
        Notification notification = new Notification.Builder(this)
                .setOngoing(true)
                .setContentTitle("New Notifications")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentIntent(pendingIntent)
                .setLights(Color.WHITE,500,500)
                .setShowWhen(true)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setSound(ringtone,AudioManager.STREAM_MUSIC)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(3,notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // super.onStartCommand(intent, flags, startId);
       String string= intent.getStringExtra("EXTRA");

       if(string!=null)
       {
           startMyOwnForeground(string);

       }

       if(string==null) {
           startTimer();

       }

       return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReceiveSms.class);
        this.sendBroadcast(broadcastIntent);
    }
    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  "+ (counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

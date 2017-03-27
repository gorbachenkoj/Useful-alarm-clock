package com.example.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import static java.util.Calendar.getInstance;

public class MainActivity extends AppCompatActivity {
    private Button saveTime;
    private Button turnOff;
    private AlarmManager alarmManager;
    private TimePicker timePicker;
    private PendingIntent pendingIntent;
    private NotificationManager notificationManager;
    private Calendar calendar;
    private Intent intent;
    private String sream  = "http://www.radiorecord.ru/radio/stations/?st=rock";
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveTime = (Button) findViewById(R.id.save_time);
        turnOff = (Button) findViewById(R.id.turn_off_alarm);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        timePicker = (TimePicker) findViewById(R.id.timePicker2);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        calendar = Calendar.getInstance();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask.execute(sream);

        saveTime.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                intent = new Intent(MainActivity.this, Alarm_Recerver.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingIntent);

            }
        });

    }
    class PlayerTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                mediaPlayer.setDataSource(params[0]);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

    }

//    void sendNotif (PendingIntent pendingIntent){
//        Notification notification = new Notification(R.mipmap.ic_launcher, "Notif", System.currentTimeMillis() );
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//    }

}

package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private static final String CHANNEL_ID = "MusicServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicService", "onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if ("ACTION_TOGGLE_PAUSE".equals(action)) {
            pauseMusic();
            return START_NOT_STICKY;
        }






    int songId = intent.getIntExtra("song_id", R.raw.f1_audio);

        createNotificationChannel();


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Playing")
                .setContentText("Your song is playing")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        startForeground(1, notification);


        mediaPlayer = MediaPlayer.create(this, songId);
        mediaPlayer.start();

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MusicService", "onDestroy called");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            serviceChannel.setSound(null, null);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
                Log.d("MusicService", "Notification channel created.");
            } else {
                Log.e("MusicService", "NotificationManager is null.");
            }
        }
    }
    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        else {
            mediaPlayer.start();

        }
    }

}

package com.example.laurdroid.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.laurdroid.R;
import com.example.laurdroid.RandomMovieActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Open the RandomMovie activity when the notification is clicked
        Intent randomMovieIntent = new Intent(context, RandomMovieActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, randomMovieIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                .setSmallIcon(R.drawable.ic_loading)
                .setContentTitle("Discover a new movie!")
                .setContentText("Click here to find a random movie.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        Log.v("Notif", "Notif shown");
    }
}


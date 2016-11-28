package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

// http://simpleandroidtutorials.blogspot.ca/2012/06/periodically-update-data-from-server-in.html 2016-11-12, 3:02 PM, author Nirali

/**
 * Periodically polls the server to update Rider and Driver's openRequests and acceptedRequests. Sends notifications
 * if a Rider or Driver has accepted one of their open/accepted requests.
 */
public class PollingService extends IntentService {
    public PollingService() {
        super("MyServiceName");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("PollingService", "About to execute MyTask");
        Rider.getInstance().updateOpenRequests();
        Driver.getInstance().updateAcceptedRequests();
        if (Rider.getInstance().driverHasAccepted() && !Rider.getInstance().hasReceivedNotification()) {
            final NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                            setContentTitle("Driver ready").setContentText("A driver is ready to pick you up.");
            Intent resultIntent = new Intent(this, RiderActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(RiderActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            final int notificationId = 1;
            final NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, builder.build());
            Rider.getInstance().setReceivedNotification(true);
            Log.i("send notification", "rider");
        }
        if (Driver.getInstance().riderHasAccepted() != null && !Driver.getInstance().hasReceivedNotification()) {
            final NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                            setContentTitle("Rider ready").setContentText("The rider is ready to be picked up.");
            Intent resultIntent = new Intent(this, DriverActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(DriverActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            final int notificationId = 1;
            final NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId, builder.build());
            Driver.getInstance().setReceivedNotification(true);
            Log.i("send notification", "driver");
        }
        Log.i("driver accepted", ((Boolean)Rider.getInstance().driverHasAccepted()).toString());
        Log.i("rider accepted", ((Boolean)(Driver.getInstance().riderHasAccepted()!=null)).toString());
    }
}

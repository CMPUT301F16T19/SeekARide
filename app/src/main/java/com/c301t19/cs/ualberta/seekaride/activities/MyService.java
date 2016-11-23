package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

/**
 * Created by Master Chief on 2016-11-23.
 */
public class MyService extends IntentService {
    public MyService() {
        super("MyServiceName");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("MyService", "About to execute MyTask");
        Rider.getInstance().updateOpenRequests();
        Driver.getInstance().updateAcceptedRequests();
        if (Rider.getInstance().driverHasAccepted()) {
            final NotificationCompat.Builder Abuilder =
                    new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                            setContentTitle("Driver ready").setContentText("The Driver will pick you up shortly.");
            final int Anotificationid = 1;
            final NotificationManager Anotifymang =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Anotifymang.notify(Anotificationid, Abuilder.build());
        }
        if (Driver.getInstance().riderHasAccepted()) {
            final NotificationCompat.Builder Abuilder =
                    new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                            setContentTitle("Rider ready").setContentText("The Rider is ready to be picked up.");
            final int Anotificationid = 1;
            final NotificationManager Anotifymang =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Anotifymang.notify(Anotificationid, Abuilder.build());
        }
    }
}

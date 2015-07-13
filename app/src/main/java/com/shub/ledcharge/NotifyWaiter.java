package com.shub.ledcharge;

import android.app.NotificationManager;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by matteotenca on 12/07/15.
 */

class NotifyWaiter extends AsyncTask<NotificationManager, Void, Void> {

    int total;


    @Override
    protected Void doInBackground(NotificationManager... notificationManagers) {

        NotificationManager mNManager = notificationManagers[0];



        try {
            synchronized (mNManager)
            {

                mNManager.wait();
                Log.e("!___!", "Wait finished");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}

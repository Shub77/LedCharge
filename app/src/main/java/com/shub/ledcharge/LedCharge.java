package com.shub.ledcharge;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.app.PendingIntent.getActivity;


public class LedCharge extends FragmentActivity {

    private BroadcastReceiver br;
    private IntentFilter mIntentFilter;
    private String chargingMode = "";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyMgr;
    NotificationManager mNmanager;
    Notification mNotification;
    private boolean firstrun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_charge_led);

        Context con = this.getApplicationContext();


        Intent mServiceIntent;
        /*

        mServiceIntent = new Intent(getActivity(), Bservice.class);
        mServiceIntent.se


        */


        // Uno - preara la notifica
        //PrepareLedNotification();



        // Due - crea il broadcast che quando ricetve l'Intent fa il notify() sbloccando WaitLedNotification
        br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent){


                //e lancia il thread con il wait

                //new NotifyWaiter().execute(mNmanager);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;


                    if (isCharging) {


                        Log.e("!___!", "Battery is charging");


                        // How are we charging?
                        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
                        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

                        if (usbCharge) {
                            Log.e("!___!", "USB battery charging");
                            if ( ! chargingMode.contains("USB") ) {
                                chargingMode = "USB";
                                if (! firstrun ) { PrepareLedNotification(); }
                                firstrun = false;
                            }

                            //synchronized (mNmanager) {
                                //mNmanager.notifyAll();
                            //}


                        }
                        else if (acCharge) {
                            Log.e("!___!", "AC battery charging");
                            if ( ! chargingMode.contains("AC") ) {
                                chargingMode = "AC";
                                if (! firstrun ) { PrepareLedNotification(); }
                                firstrun = false;
                            }


                        }
                        else {
                            Log.e("!___!", "Unknown battery charging");
                            Log.e("!___!", "AC battery charging");
                            if ( ! chargingMode.contains("Unknown") ) {
                                chargingMode = "Unknown";
                                if (! firstrun ) { PrepareLedNotification(); }
                                firstrun = false;
                            }
                        }
                    }
                    else {
                        Log.e("!___!", "Battery is not charging");
                        Log.e("!___!", "AC battery charging");
                        if ( ! chargingMode.contains("Not charging") ) {
                            chargingMode = "Not charging";
                            if (! firstrun ) { PrepareLedNotification(); }
                            firstrun = false;
                        }
                    }


            }
        };


        mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br, mIntentFilter);


    }

    private void PrepareLedNotification()
    {

        /*

        mNmanager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE );
        mNotification = new Notification();
        mNotification.flags = Notification.FLAG_SHOW_LIGHTS;
        mNotification.ledOnMS = 100000;
        mNotification.ledOffMS = 1;
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;

        */

        long[] vibrate = new long[]{10, 100, 500};

        mBuilder = new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.notification_icon)
                //.setContentTitle("My notification")
                //.setContentText("Hello World!")
                //.setLights(Color.GRAY , 1000, 100)
                //.setAutoCancel(true)
                .setVibrate(vibrate);
        //Notification mNotification = mBuilder.build();


        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service

        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        Log.e("!___!", "Vibrate");
        mNotifyMgr.notify(mNotificationId, mBuilder.build());




    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

}

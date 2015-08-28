package com.seaman.andrew.homeautomation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * File: ConnectionService.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the service that supplies the WiFi connection
 *  to the ESP_server. This service will only be started once per app session, right
 *  after log in. On connect, it will send a "hello" signal to each of the
 *  WiFi hubs. On receipt, the hubs will ping each of the relay hubs in order to
 *  check their connectivity status. The service has a dedicated network thread, in
 *  order to separate network activity from the main thread.
 *
 *  On receiving a broadcast, the service will append the message to the blocking queue
 *  that is accessed by the network thread. The service will also handle notifications
 *  regarding the status of the network connection and the WiFi hubs.
 *
 *  Current Progress (incomplete):
 *  - WiFi connection in place
 *  - Network thread created and implemented
 *  - Blocking queue created and implemented
 *  - Broadcast receiver in place
 *
 *  TODO: notification manager
 *  TODO: receiving messages from WiFi hub and relaying back to activities
 *
 */

public class ConnectionService extends Service {
    // to log activity to log.file
    private ActivityLogger log = new ActivityLogger();

    // hopefully used to build notifications for the notification bar (not implemented)
    private Notification.Builder notifyBuilder;

    // thread that runs all network related things through service
    private Thread networkThread;

    // runnable for thread
    private NetworkThreadRunnable networkRunnable;

    // receives incoming broadcasts (only forwards string extras to blocked queue)
    private ConnectionBroadcastReceiver cbReceiver;

    // synchronous queue for string input, network thread is consumer
    BlockingQueue<String> blockingQueue;

    private IntentFilter filter;

    public ConnectionService() {
    }

    @Override
    public void onCreate() {
        notifyBuilder = new Notification.Builder(this);
        blockingQueue = new LinkedBlockingQueue<String>();
        networkRunnable = new NetworkThreadRunnable(blockingQueue);

        // registering receiver
        cbReceiver = new ConnectionBroadcastReceiver();
        // add filters if other types of broadcasts are necessary
        filter = new IntentFilter("seaman.andrew.homeautomation.MESSAGE");

        // checking server connection and network thread
        blockingQueue.add("Hello server!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // re-register receiver
        this.registerReceiver(cbReceiver, filter);

        // begin network thread when service is started
        networkThread = new Thread(networkRunnable);
        networkThread.start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // unregister broadcast receiver
        unregisterReceiver(cbReceiver);
        // end network thread via runnable
        networkRunnable.endRunnable();
    }

    // should eventually show notifications in the notification bar
    // not working yet...
    private void showNotification(String text) {
        notifyBuilder.setContentTitle(Constants.NOTIFICATION_HEADER);
        notifyBuilder.setContentText(text);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, notifyBuilder.getNotification());
    }

    // implementation of broadcast receiver
    public class ConnectionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            // if there are extras, add these to the blocking queue to be picked up by the network thread
            if (extras != null) {
                if (extras.containsKey("MESSAGE")) {
                    String message = extras.get("MESSAGE").toString();
                    blockingQueue.add(message);
                }
            }
        }

    }

}

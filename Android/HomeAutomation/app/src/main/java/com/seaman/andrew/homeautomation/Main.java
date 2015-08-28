package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import java.io.File;

/*
 * File: Main.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the main menu for the application. It is
 *  viewed directly after logging in and allows the user to navigate to the
 *  desired activity.
 *
 */

public class Main extends Activity {
    // Used to log to log.file
    ActivityLogger log = new ActivityLogger();

    public static String MAC_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final Context context = this;
        setContentView(R.layout.activity_main);

        // sets MAC_ADDRESS to the device's MAC, for use in sent messages
        getMacAddress();

        // Get Extras sent from LoginActivity (username)
        String username = intent.getStringExtra(LoginActivity.USER);

        // Set title bar in window
        this.setTitle(Constants.WELCOME + username);

        // Set up file structure
        createFileStructure();

        // Button Setup
        final Button roomsButton = (Button) findViewById(R.id.roomsButton);
        roomsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open selectRoomActivity when Rooms button is clicked
                Intent intent = new Intent(v.getContext(), SelectRoomActivity.class);
                startActivity(intent);
            }
        });

        final Button addElementsButton = (Button) findViewById(R.id.addElementsButton);
        addElementsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open addElementsActivity on add elements button click
                Intent intent = new Intent(v.getContext(), AddElementsActivity.class);
                startActivity(intent);
            }
        });

        final Button removeElementsButton = (Button) findViewById(R.id.removeElementsButton);
        removeElementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open removeElementsActivity when remove elements button is clicked
                Intent intent = new Intent(view.getContext(), RemoveElementsActivity.class);
                startActivity(intent);
            }
        });

        // contains input in message edit text field, used for checking server connection
        final EditText msgField = (EditText) findViewById(R.id.inputMessageText);

        // connect button, used now for checking broadcast sent to service
        final Button connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get message from editText field
                String text = msgField.getText().toString();

                Msg message = new Msg(MAC_ADDRESS, 'e', 't', '1', 'x');

                System.out.println("MAC: " + MAC_ADDRESS);

                String sendmsg = message.toSendString();
                System.out.println("send: " + sendmsg);

                // Intent to send message by broadcast (to ConnectionService)
                Intent msgIntent = new Intent();
                msgIntent.setAction("seaman.andrew.homeautomation.MESSAGE");
                msgIntent.putExtra("MESSAGE", sendmsg);
                sendBroadcast(msgIntent);

                // Clear editText field
                msgField.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createFileStructure() {
        // Create /HomeAutomation folder if doesn't exist
        File dir = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.HOME_FOLDER);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        // Create /HomeAutomation/Data folder if doesn't exist
        dir = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.DATA_FOLDER);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        // Create /HomeAutomation/Data/Rooms folder if doesn't exist
        dir = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER);
        if (!dir.exists()) {
            try {
                dir.mkdir();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void getMacAddress() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        MAC_ADDRESS = wInfo.getMacAddress();
    }
}

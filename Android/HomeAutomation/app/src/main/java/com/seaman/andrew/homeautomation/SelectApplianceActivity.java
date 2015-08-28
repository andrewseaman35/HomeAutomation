package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * File: SelectApplianceActivity.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the activity to select an appliance to alter
 *  the state of. The listed appliances are dependent on the room that is selected
 *  in SelectRoomActivity
 *
 *  At this point, all of the appliances are "toggle" appliances, so all the list
 *  items only have an on/off switch. In the future, other types of appliances will
 *  be added and there will have to be additional list item types.
 *
 * Current Progress (incomplete)
 *  - Display items as proper views
 *  - Send message on toggle
 *
 *  TODO: send proper message
 *  TODO: await response after message is sent
 *
 */

public class SelectApplianceActivity extends Activity {

    private ListView applianceListView;
    private ToggleListAdapter<String> toggleAdapter;

    private String inRoom;
    private String[] appliances;
    private int[] applianceIDs;
    private int numAppliances;
    private ArrayList<Appliance> applianceArrayList;

    private int[] toggleStatus;

    private File roomFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appliance);
        Intent intent = getIntent();
        Context context = this;

        // Begin network service
        //startService(new Intent(context, ConnectionService.class));

        // Get extras from select room activity (room that is selected)
        inRoom = intent.getStringExtra(SelectRoomActivity.ROOM);

        // General UI Setup
        this.setTitle(inRoom);

        // Get appliances in room
        roomFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER, inRoom + ".CSV");
        if(!roomFile.exists())
        {
            finish();
        }

        // puts all appliances into appliancesList
        applianceArrayList = new ArrayList<Appliance>();

        // if there aren't any appliances in the room, only appliance is NO_APPLIANCES
        if((numAppliances = getAppliances()) == 0)
        {
            applianceArrayList.add(new Appliance(Constants.NO_APPLIANCES, "Toggle", -1, Constants.INITIAL_STATUS));
        } else {
            // start with all off for now
            toggleStatus = new int[numAppliances];
            // Todo: get states from server
            // Todo: get appliance IDs when populating lists
            for(int i = 0; i < numAppliances; i++)
            {
                toggleStatus[i] = 0;
            }
        }

        for(int i = 0; i < appliances.length; i++) {
            applianceArrayList.add(new Appliance(appliances[i], "Toggle", applianceIDs[i], toggleStatus[i]));
        }

        applianceListView = (ListView)findViewById(R.id.applianceListView);
        toggleAdapter = new ToggleListAdapter<String>(context, applianceArrayList);
        applianceListView.setAdapter(toggleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_appliance, menu);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int getAppliances()
    {
        String splitBy = ",";
        BufferedReader buffRead;
        String line;
        ArrayList<String> appls = new ArrayList<String>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        try {
            buffRead = new BufferedReader(new FileReader(roomFile));

            while((line = buffRead.readLine()) !=null){
                appls.add(line.split(splitBy)[Constants.SPLIT_NAME]);
                ids.add(Integer.parseInt(line.split(splitBy)[Constants.SPLIT_ID]));
            }
            buffRead.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if(!appls.isEmpty()) {
            appliances = new String[appls.size()];
            appliances = appls.toArray(appliances);
            applianceIDs = new int[appls.size()];
            for(int i = 0; i < appls.size(); i++) {
                applianceIDs[i] = ids.get(i);
            }
        }
        return appls.size();
    }
}

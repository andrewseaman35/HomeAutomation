package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class SelectRoomActivity extends Activity {
    public final static String ROOM = "com.seaman.andrew.homeautomation.SelectApplianceActivity.ROOM";
    private final File roomsFolder = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER);

    private ListView roomListView;
    private ArrayAdapter<String> roomsAdapter;

    private String[] roomStringList;
    private File[] roomFileList;
    private int numberOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Initial UI Setup
        this.setTitle(Constants.SELECT_ROOM);

        // Get rooms from file and store in roomsList
        if(getAvailableRooms() == 0){
            roomStringList = new String[1];
            roomStringList[0] = Constants.NO_ROOMS;
        }

        // Set up ListView
        roomsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomStringList);
        roomListView = (ListView)findViewById(R.id.roomListView);
        roomListView.setAdapter(roomsAdapter);

        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
                String roomName = roomsAdapter.getItem(pos);
                if (!roomStringList[pos].equals(Constants.NO_ROOMS)) {
                    startSelectApplianceActivity(roomName);
                } else {
                    Toast.makeText(v.getContext(), Constants.NO_ROOMS, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room, menu);
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

    private int getAvailableRooms()
    {
        roomFileList = roomsFolder.listFiles().clone();
        int amtRooms = roomFileList.length;

        if(amtRooms >  0) {
            roomStringList = new String[amtRooms];
            for (int i = 0; i < amtRooms; i++) {
                roomStringList[i] = roomsFolder.listFiles()[i].getName().substring(0, roomsFolder.listFiles()[i].getName().length()-4);
            }
        }
        return amtRooms;
    }

    private void startSelectApplianceActivity(String roomName)
    {
        Intent intent = new Intent(this, SelectApplianceActivity.class);
        intent.putExtra(ROOM, roomName);
        startActivity(intent);
    }
}

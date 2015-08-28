package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * File: RemoveElementsActivity.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the activity to remove elements from the
 *  file system. It allows the user to remove entire rooms, or individual
 *  appliances in the room.
 *
 * Current Progress (incomplete)
 *  - Remove room added
 *  - Remove appliance added
 *
 */

public class RemoveElementsActivity extends Activity {
    private final int NEW_LINE = 10;

    private final Context context = this;

    private ActivityLogger log = new ActivityLogger();
    private final File roomsFolder = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER);

    private Spinner removeRoomSpinner;
    private Spinner removeApplianceSpinner;

    private ArrayAdapter<String> removeRoomAdapter;
    private ArrayAdapter<String> removeApplianceAdapter;

    private String[] roomNameList;
    private File[] roomFileList;
    private int numRooms;
    private int curRoomPos = 0;
    private int curApplPos = 0;

    private String[] applianceNameList;

    private Button removeRoomButton;
    private Button removeApplianceButton;

    private Boolean roomSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_elements);

        roomSelected = false;

        // Initial UI Setup
        this.setTitle("Remove Elements");

        // Button setup
        removeRoomButton = (Button)findViewById(R.id.removeRoomButton);
        removeApplianceButton = (Button)findViewById(R.id.removeApplianceButton);

        // get all available rooms
        numRooms = getRoomList();
        if(numRooms == 0) {
            roomNameList = new String[1];
            roomNameList[0] = Constants.NO_ROOMS;
        }

        // set up room spinner (appliance spinner set up after room is selected)
        removeApplianceSpinner = (Spinner)findViewById(R.id.removeApplianceSpinner);
        removeRoomSpinner = (Spinner)findViewById(R.id.removeRoomSpinner);
        removeRoomAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, roomNameList);
        removeRoomSpinner.setAdapter(removeRoomAdapter);

        removeRoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                curRoomPos = pos;
                setUpApplianceSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        removeApplianceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                curApplPos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        removeRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!roomNameList[curRoomPos].equals(Constants.NO_ROOMS)) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    alertDialogBuilder.setTitle(Constants.CONFIRM_DELETE);

                    alertDialogBuilder
                            .setMessage(Constants.DELETE_ROOM_DIALOG)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removeRoom();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();
                }
                else
                {
                    Toast.makeText(context, Constants.NO_ROOMS, Toast.LENGTH_SHORT).show();
                }
            }
        });

        removeApplianceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!applianceNameList[curApplPos].equals(Constants.NO_APPLIANCES))
                {
                    File roomFile = roomFileList[curRoomPos];
                    int removeID = removeApplianceFromRoom(roomFile, applianceNameList[curApplPos]);
                    removeIdFromList(removeID);

                }
                else
                {
                    Toast.makeText(context, Constants.NO_APPLIANCES, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    // method to remove the specified room from the file structure
    private void removeRoom() {
        // array list holds the ids of all of the appliances in the room
        ArrayList<Integer> ids = new ArrayList<Integer>();

        String line;
        try {
            // bufferedReader reads in all lines of the room file in order to get the ids
            BufferedReader buffRead = new BufferedReader(new FileReader(roomFileList[curRoomPos]));
            while((line = buffRead.readLine()) != null) {
                ids.add(Integer.parseInt(line.split(",")[Constants.SPLIT_ID]));
            }
            buffRead.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // remove all the IDs that are found in the room file
        for(int i = 0; i < ids.size(); i++) {
            removeIdFromList(ids.get(i));
        }

        // delete the room file, refresh spinner,  and log
        try {
            roomFileList[curRoomPos].delete();
            refreshRoomSpinner();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, Constants.ROOM_REMOVED + roomNameList[curRoomPos], Toast.LENGTH_SHORT).show();
        log.addLog(Constants.ROOM_REMOVED + roomNameList[curRoomPos]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_remove_elements, menu);
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

    private int removeApplianceFromRoom(File roomFile, String appliance) {
        int len = (int) roomFile.length();

        File tempFile = new File(roomsFolder, Constants.REMOVE_APPL_TEMP_FILE);

        BufferedReader buffRead;
        BufferedWriter buffWrite;

        String inLine;

        int idToRemove = -1;

        String splitBy = ",";

        ArrayList<Appliance> applList = new ArrayList<Appliance>();

        // create a temp file to write to, will become final room file
        if(!tempFile.exists())
        {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            buffRead = new BufferedReader(new FileReader(roomFile));

            while((inLine = buffRead.readLine()) !=null){
                Appliance newAppl = new Appliance(inLine.split(splitBy)[Constants.SPLIT_NAME],
                        inLine.split(splitBy)[Constants.SPLIT_TYPE],
                        Integer.parseInt(inLine.split(splitBy)[Constants.SPLIT_ID]),
                        Integer.parseInt(inLine.split(splitBy)[Constants.SPLIT_STATUS]));
                if(newAppl.name.equals(appliance)) {
                    idToRemove = newAppl.id;
                } else {
                    applList.add(newAppl);
                }
            }
            buffRead.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffWrite = new BufferedWriter(new FileWriter(tempFile));
            String type = "Toggle";

            for(int i = 0; i < applList.size(); i++) {
                buffWrite.write(applList.get(i).name + "," + type + ","
                        + String.valueOf(applList.get(i).id) + ","
                        + String.valueOf(applList.get(i).state + "\n"));
            }
            buffWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(context, "Room removed: " + appliance, Toast.LENGTH_SHORT).show();
        log.addLog("Room removed: " + appliance);

        // rename new room file to old file name
        tempFile.renameTo(roomFile);

        // refresh spinners
        refreshRoomSpinner();
        setUpApplianceSpinner();

        removeRoomSpinner.setSelection(curRoomPos);

        return idToRemove;
    }

    // removes a single id indicator from id.file
    private void removeIdFromList(int id) {
        // File for id.file
        File idFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.DATA_FOLDER + "/" + Constants.ID_FILE_NAME);

        // array to contain contents of original file
        int[] idsList = new int[Constants.MAX_AMOUNT_IDS];

        try {
            // buffered reader reads all of the data from id.file into idsList
            BufferedReader buffRead = new BufferedReader(new FileReader(idFile));
            for(int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                idsList[i] = (buffRead.read()-48);
            }
            buffRead.close();

            // change the id in idsList to indicate that it is available
            idsList[id] = 0;

            // BufferedWriter to write data back to id.file
            BufferedWriter buffWrite = new BufferedWriter(new FileWriter(idFile, false));
            for(int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                buffWrite.write(String.valueOf(idsList[i]));
            }
            buffWrite.close();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private int getRoomList()
    {
        int amtRooms = roomsFolder.listFiles().length;
        if(amtRooms >  0) {
            roomFileList = roomsFolder.listFiles();
            roomNameList = new String[amtRooms];
            for (int i = 0; i < amtRooms; i++) {
                if(roomFileList[i].getName().length()-4 != 0) {
                    // subtring removes the ".CSV" from the list so only room name shows up
                    roomNameList[i] = roomFileList[i].getName().substring(0, roomFileList[i].getName().length() - 4);
                }
            }
        }
        return amtRooms;
    }

    private void setUpApplianceSpinner()
    {
        if(removeApplianceSpinner.getAdapter() != null) {
            removeApplianceSpinner.setAdapter(null);
        }

        if(getAppliances() == 0) {
            applianceNameList = new String[1];
            applianceNameList[0] = Constants.NO_APPLIANCES;
        }

        removeApplianceAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, applianceNameList);
        removeApplianceSpinner.setAdapter(removeApplianceAdapter);
    }

    private void refreshRoomSpinner()
    {
        if(getRoomList() == 0)
        {
            roomNameList = new String[1];
            roomNameList[0] = Constants.NO_ROOMS;
        }

        removeRoomAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, roomNameList);
        removeRoomSpinner.setAdapter(removeRoomAdapter);
    }

    private int getAppliances()
    {
        String splitBy = ",";
        BufferedReader buffRead;
        int numAppl = 0;
        String line;
        ArrayList<String> appls = new ArrayList<String>();
        try {
            buffRead = new BufferedReader(new FileReader(roomFileList[curRoomPos]));

            while((line = buffRead.readLine()) !=null){
                ++numAppl;
                appls.add(line.split(splitBy)[Constants.SPLIT_NAME]);
            }
            buffRead.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if(numAppl > 0) {
            applianceNameList = new String[appls.size()];
            applianceNameList = appls.toArray(applianceNameList);
        }
        return numAppl;
    }

}



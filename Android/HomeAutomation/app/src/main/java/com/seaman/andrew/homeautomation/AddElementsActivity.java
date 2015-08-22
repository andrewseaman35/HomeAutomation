package com.seaman.andrew.homeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;


public class AddElementsActivity extends Activity {
    private final int intTAKEN = 1;
    private final String stringTAKEN = "1";
    private final int intAVAILABLE = 0;
    private final String stringAVAILABLE = "0";

    private ActivityLogger log = new ActivityLogger();
    private FileWriteThreader fileWriteThreader = new FileWriteThreader();

    private final File roomsFolder = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER);
    File idFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.DATA_FOLDER + "/" + Constants.ID_FILE_NAME);

    private Spinner addApplianceSpinner;
    private ArrayAdapter<String> addApplianceSpinnerAdapter;
    private String[] roomNameList;
    private File[] roomFileList;
    private int numRooms;

    private Spinner applianceTypeSpinner;

    private String[] appliancesInSelectedRoom;
    private String applianceType;
    private int addToRoomPos;
    private Button addRoomsButton;
    private Button addApplianceButton;
    private EditText addRoomsField;
    private EditText addApplianceField;

    private int[] idsTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_elements);

        // Initial UI Setup
        this.setTitle(Constants.ADD_ELEMENTS_TITLE);

        // Get rooms
        numRooms = getRoomList();
        if(numRooms == 0) {
            roomNameList = new String[1];
            roomNameList[0] = Constants.NO_ROOMS;
        }

        idsTaken = new int[Constants.MAX_AMOUNT_IDS];

        // Set up spinners
        addApplianceSpinner = (Spinner) findViewById(R.id.addApplianceSpinner);
        addApplianceSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1, roomNameList);
        addApplianceSpinner.setAdapter(addApplianceSpinnerAdapter);

        applianceTypeSpinner = (Spinner)findViewById(R.id.applianceTypeSpinner);

        // Set up button
        addRoomsButton = (Button)findViewById(R.id.addRoomButton);
        addApplianceButton = (Button)findViewById(R.id.addApplianceButton);

        // Set up edittexts
        addRoomsField = (EditText)findViewById(R.id.addRoomInputText);
        addApplianceField = (EditText)findViewById(R.id.addApplianceInputText);

        // Set up listeners
        addApplianceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int pos, long id) {
                addToRoomPos = pos;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // todo
            }
        });

        applianceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                applianceType = (String) applianceTypeSpinner.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addApplianceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get name of new appliance and room to add it to
                if (!addApplianceField.getText().toString().equals(Constants.NO_ROOMS)) {
                    String newApplianceName = addApplianceField.getText().toString();
                    String addToRoom = roomNameList[addToRoomPos];

                    // File that corresponds to room
                    File roomFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER, addToRoom + ".CSV");
                    if (!roomFile.exists()) {
                        try {
                            roomFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // obtain available ID from id.file
                    int newID = getNewID();

                    // get the list of appliances in the current room
                    int numAppliances = getAppliancesInRoom();

                    // if the appliance doesn't already exist in the room
                    if (!applianceIsInRoom(newApplianceName, numAppliances)) {
                        // append new appliance information to file
                        fileWriteThreader.write(newApplianceName + "," + applianceType + "," + newID + "," + Constants.INITIAL_STATUS + "\n",
                                roomFile, true);

                        // log, toast, and reset fields
                        log.addLog("Appliance added: " + newApplianceName + " to " + addToRoom);
                        Toast.makeText(view.getContext(), "Added " + newApplianceName + " to " + addToRoom, Toast.LENGTH_SHORT).show();
                        addApplianceField.setText("");
                        addApplianceField.setHint("Appliance Name");

                        // if appliance field is empty, toast
                    } else if (newApplianceName.isEmpty()) {
                        Toast.makeText(view.getContext(), Constants.ENTER_APPLIANCE_NAME, Toast.LENGTH_SHORT).show();

                        // if the appliance already exists, toast
                    } else {
                        Toast.makeText(view.getContext(), Constants.APPLIANCE_ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                    }

                    // if there are no rooms available, toast
                } else {
                    Toast.makeText(view.getContext(), Constants.NO_ROOMS, Toast.LENGTH_SHORT).show();
                }
            }

        });

        addRoomsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get new room name from field
                String newRoomName = addRoomsField.getText().toString();

                // if room name is there
                if(!newRoomName.isEmpty()) {
                    // check to see if the room already exists
                    File newRoom = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.ROOMS_FOLDER, newRoomName + ".CSV");
                    if (!newRoom.exists())
                    {
                        try
                        {
                            // create a new file with the room name
                            newRoom.createNewFile();

                            // log, toast, reset fields
                            log.addLog(Constants.ROOM_ADDED + newRoomName);
                            Toast.makeText(view.getContext(), Constants.ROOM_ADDED + newRoomName, Toast.LENGTH_SHORT).show();
                            addRoomsField.setText("");
                            addRoomsField.setHint("Room Name");

                            // refresh room spinner for addAppliance
                            if(getRoomList() == 0)
                            {
                                roomNameList = new String[1];
                                roomNameList[0] = Constants.NO_ROOMS;
                            }
                            addApplianceSpinnerAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.simple_list_item_1, roomNameList);
                            addApplianceSpinner.setAdapter(addApplianceSpinnerAdapter);
                        }
                        catch (IOException e)
                        {
                            Toast.makeText(view.getContext(), Constants.ERR_ROOM_ADD, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        // if room exists, toast
                    } else {
                        Toast.makeText(view.getContext(), Constants.ROOM_ALREADY_EXISTS, Toast.LENGTH_SHORT).show();
                    }

                    // if no room name is inputted in field, toast
                } else {
                    Toast.makeText(view.getContext(), Constants.ENTER_ROOM_NAME, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        writeIDFile();

    }

    @Override
    protected void onResume() {
        super.onResume();
        createIDsTakenArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_elements, menu);
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

    private boolean applianceIsInRoom(String newRoom, int numAppls)
    {
        for(int i = 0; i < numAppls; i++)
        {
            if(newRoom.equals(appliancesInSelectedRoom[i]))
            {
                return true;
            }
        }
        return false;
    }

    private int getAppliancesInRoom()
    {
        String splitBy = ",";
        BufferedReader buffRead;
        int numAppl = 0;
        String line;
        ArrayList<String> appls = new ArrayList<String>();
        try {
            buffRead = new BufferedReader(new FileReader(roomFileList[addToRoomPos]));

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
            appliancesInSelectedRoom = new String[appls.size()];
            appliancesInSelectedRoom = appls.toArray(appliancesInSelectedRoom);
        }
        return numAppl;
    }

    private int getRoomList()
    {
        int amtRooms = roomsFolder.listFiles().length;
        if(amtRooms >  0) {
            roomFileList = roomsFolder.listFiles();
            roomNameList = new String[amtRooms];
            for (int i = 0; i < amtRooms; i++) {
                roomNameList[i] = roomFileList[i].getName().substring(0, roomFileList[i].getName().length()-4);
            }
        }
        return amtRooms;
    }

    private void createIDsTakenArray() {
        // if the file has not yet been created, create it and populate with zeros
        // the index of the zero indicates the ID
        // a zero means that it has not been taken
        // a one means that it has been taken
        if (!idFile.exists()) {
            try {
                idFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // kept this one without fileWriteThreader to avoid continuous opening and closing of fileWriter
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(idFile, true));
                for (int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                    buf.append(stringAVAILABLE);
                }
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                idsTaken[i] = intAVAILABLE;
            }

            // otherwise, read from file
        } else {
            try {
                BufferedReader buffRead = new BufferedReader(new FileReader(idFile));
                for (int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                    idsTaken[i] = Integer.parseInt(Character.toString((char) buffRead.read()));
                }
                buffRead.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeIDFile() {
        //BufferedWriter for performance, true to set append to file flag
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(idFile, false));
            for (int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                if (idsTaken[i] == intAVAILABLE) {
                    buf.write(stringAVAILABLE);
                } else {
                    buf.write(stringTAKEN);
                }
            }
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readIDFile() {
        try {
            BufferedReader buffRead = new BufferedReader(new FileReader(idFile));
            for (int i = 0; i < Constants.MAX_AMOUNT_IDS; i++) {
                idsTaken[i] = buffRead.read();
            }
            buffRead.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private int getNewID() {
        int i = 0;
        while(i < Constants.MAX_AMOUNT_IDS) {
            if(idsTaken[i] != intAVAILABLE)
                ++i;
            else
                break;
        }
        if(i >= Constants.MAX_AMOUNT_IDS) {
            i = Constants.MAX_AMOUNT_IDS - 1;
        }
        idsTaken[i] = intTAKEN;

        return i;
    }
}

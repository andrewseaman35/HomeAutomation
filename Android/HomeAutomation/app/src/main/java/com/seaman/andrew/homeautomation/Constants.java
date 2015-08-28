package com.seaman.andrew.homeautomation;

import android.os.Environment;

import java.io.File;

/*
 * File: Constants.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains constant values that are used throughout the application
 *
 */
public class Constants {
    // Constant Values
    public static final String ENCODING = "UTF-8";
    public static int MAX_APPLIANCE_NAME_LENGTH = 25; // must be changed in activity_add_elements.xml as well
    public static int MAX_TYPE_LENGTH = 15; // must be changed in activity_add_elements.xml as well
    public static int MAX_AMOUNT_IDS = 128;
    public static int INITIAL_STATUS = 0;

    // General UI Constants
    public static final String WELCOME = "Welcome, ";
    public static final String SELECT_ROOM = "Select Room";
    public static final String SELECT_APPLIANCE = "Select Appliance";
    public static final String ADD_ELEMENTS_TITLE = "Add Elements";
    public static final String CONFIRM_DELETE = "Confirm Delete?";
    public static final String DELETE_ROOM_DIALOG = "Deleting a room will delete all of its appliances. \n" +
            "Are you sure you would like to delete?";

    // Activity Logger Constants
    public static final String USER_LOGIN = "User logged in: ";
    public static final String CONNECTED_TO_SERVER = "Connected to server: ";
    public static final String NOT_CONNECTED_TO_SERVER = "Failed to connect to server: ";
    public static final String DISCONNECTED_FROM_SERVER = "Disconnected from server";
    public static final String MESSAGE_SENT = "Message sent to server";
    public static final String MESSAGE_RECEIVED = "Message received from server";

    // Toast Constants
    public static final String LOGIN_SUCCESS = "Successfully logged in!";
    public static final String LOGIN_FAIL = "Failed to log in";
    public static final String ROOM_ALREADY_EXISTS = "Room already exists";
    public static final String ROOM_ADDED = "Room added: ";
    public static final String ENTER_APPLIANCE_NAME = "Enter appliance name";
    public static final String ENTER_ROOM_NAME = "Enter room name";
    public static final String ROOM_REMOVED = "Room removed: ";
    public static final String APPLIANCE_ALREADY_EXISTS = "Appliance already exists";

    // Notification Constants
    public static final String NOTIFICATION_HEADER = "HomeAutomation: ";
    public static final String SERVER_CONNECTED = "Connected to server";
    public static final String SERVER_NOT_CONNECTED = "Could not connect to server";

    // Loading Text Constants
    public static final String CONNECTING_TO_SERVER = "Connecting to server";

    // File Constants
    public static final File EXTERNAL_STORAGE_DIR = Environment.getExternalStorageDirectory();
    public static final String HOME_FOLDER = "/HomeAutomation";
    public static final String LOG_FILE_NAME = "log.file";
    public static final String ID_FILE_NAME = "id.file";
    public static final String LOG_FILE = HOME_FOLDER + "/" + LOG_FILE_NAME;
    public static final String DATA_FOLDER = HOME_FOLDER + "/Data";
    public static final String ROOMS_FOLDER = DATA_FOLDER + "/Rooms";
    public static final String REMOVE_APPL_TEMP_FILE = "removeApplTemp.CSV";

    // Menu Constants
    public static final String NO_ROOMS = "No rooms found";
    public static final String NO_APPLIANCES = "No appliances found";

    // Errors
    public static final String ERR_ROOM_ADD = "Error adding room";

    // Server Constants
    public static final String HOST_IP = "192.168.1.71";
    public static final int PORT = 36330;
    public static final int TIMEOUT_LEN = 20000;

    // File split positions
    public static final int SPLIT_NAME = 0;
    public static final int SPLIT_TYPE = 1;
    public static final int SPLIT_ID = 2;
    public static final int SPLIT_STATUS = 3;

}

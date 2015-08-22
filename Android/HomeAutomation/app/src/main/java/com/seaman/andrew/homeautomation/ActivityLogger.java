package com.seaman.andrew.homeautomation;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

/*
 * File: ActivityLogger.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the ActivityLogger class. Thie purpose of this
 *   class is to provide an easy way to log application activity to a log file. An
 *   object of this class must be included in any activity that requires logging.
 *
 * Current Progress (complete)
 * - Log file is created if necessary
 * - Method implemented to write to file
 *   - I/O thread created and implemented
 */
public class ActivityLogger {
    File logFile;
    File logDir;
    FileWriteThreader fileWriteThreader;

    public ActivityLogger()
    {
        logDir = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.HOME_FOLDER);
        if (!logDir.exists())
        {
            try {
                logDir.mkdir();
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
        }

        logFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.HOME_FOLDER, Constants.LOG_FILE_NAME);
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        fileWriteThreader = new FileWriteThreader();
    };

    public void addLog(String text)
    {
        Date now = new Date();
        String mydate = DateFormat.getInstance().format(now);

        fileWriteThreader.write(mydate + " " + text + "\n", logFile, true);
   };

    public void clearLog()
    {
        logFile.delete();
        logFile = new File(Constants.EXTERNAL_STORAGE_DIR + Constants.HOME_FOLDER, Constants.LOG_FILE);
        try
        {
            logFile.createNewFile();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    };
}

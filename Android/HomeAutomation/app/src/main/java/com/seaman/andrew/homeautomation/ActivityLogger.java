package com.seaman.andrew.homeautomation;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;

/**
 * Class used to log activity of application
 * Created by Andrew on 6/30/2015.
 * Project: Home Automation
 * Version 1.0
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

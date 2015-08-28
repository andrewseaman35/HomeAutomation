package com.seaman.andrew.homeautomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * File: FileWriteThreader.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains a threader class to allow file writes to be
 *  performed outside of the main thread.
 *
 * Current Progress (incomplete)
 * - Allows single instance writing to a single file
 *
 * TODO: implement writing while looping through array or similar data structure
 */

public class FileWriteThreader {
    public void write(String nText, File nFile, boolean nAppend) {
        class Writer implements Runnable {
            String text;
            File file;
            boolean append;

            public Writer(String nText, File nFile, boolean nAppend) {
                text = nText;
                file = nFile;
                append = nAppend;
            }

            @Override
            public void run() {
                runFunc(this.text, this.file, this.append);
            }

            private void runFunc(String text, File file, boolean append) {
                try {
                    BufferedWriter buf = new BufferedWriter(new FileWriter(file, append));
                    buf.write(text);
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Thread IOThread = new Thread(new Writer(nText, nFile, nAppend));
        IOThread.run();
    }
}

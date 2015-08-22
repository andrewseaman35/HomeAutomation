package com.seaman.andrew.homeautomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Andrew on 7/31/2015.
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

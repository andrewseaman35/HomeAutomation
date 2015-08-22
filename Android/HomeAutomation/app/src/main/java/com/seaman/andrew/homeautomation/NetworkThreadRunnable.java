package com.seaman.andrew.homeautomation;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Andrew on 7/21/2015.
 */
public class NetworkThreadRunnable implements Runnable{
    // socket for server connection
    private Socket socket;

    // logger to log to log.file
    private ActivityLogger log = new ActivityLogger();

    // used to exit run loop
    private boolean quit = false;

    // will be blocking queue in connectionService
    BlockingQueue<String> blockingQueue;

    // blocking queue to be queue from connection Service
    public NetworkThreadRunnable(BlockingQueue<String> queue)
    {
        blockingQueue = queue;
    }

    public void run() {
        String message;
        // running loop
        while (!quit) {
            // while there are strings in blockingQueue, send them over server
            // todo: check validity of message after protocol is implemented before it is sent
            while ((message = blockingQueue.poll()) != null) {
                System.out.println("Trying to connect");
                if(connectToServer()) {
                    sendMessage(message);
                    disconnectFromServer();
                } else {
                    // todo: what to do if can't connect to server when message is sent
                    System.out.println("Could not connect, message added back to queue");
                    blockingQueue.add(message);
                }
            }
        }
    }

    // called by connectionService on its onDestroy
    public void endRunnable()
    {
        quit = true;
    }

    // creates connection to server
    // relies on Constants.HOST_IP and PORT, ensure that these are correct
    private boolean connectToServer()
    {
        socket = new Socket();
        try {
            // pings the server first
            Runtime.getRuntime().exec("ping " + Constants.HOST_IP);

            // connect socket to port
            socket.connect(new InetSocketAddress(Constants.HOST_IP, Constants.PORT), Constants.TIMEOUT_LEN);
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(socket.isConnected()) {
            System.out.println(Constants.CONNECTED_TO_SERVER);
            log.addLog(Constants.CONNECTED_TO_SERVER);
        } else {
            System.out.println(Constants.NOT_CONNECTED_TO_SERVER);
            log.addLog(Constants.NOT_CONNECTED_TO_SERVER);
        }

        return socket.isConnected();
    }

    private void disconnectFromServer()
    {
        try {
            socket.close();
            System.out.println("Socket disconnected");
            log.addLog(Constants.DISCONNECTED_FROM_SERVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // writes a string to the socket
    private void sendMessage(String msg)
    {
        if(socket.isConnected()) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write(msg);
                out.flush();
                log.addLog(Constants.MESSAGE_SENT);
                System.out.println(Constants.MESSAGE_SENT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // todo: if socket is not connected, keep in secondary queue?
            System.out.println("Trying to send but socket is not connected!");
        }
    }
}

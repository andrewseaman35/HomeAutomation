package com.seaman.andrew.homeautomation;

import java.math.BigInteger;
import java.util.BitSet;
/*
 * File: Msg.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the Msg class. This class is used to
 *  contain all of the required data for a complete message to be sent
 *  to the WiFi hub via protocol.
 *
 * Current Progress (complete)
 * - create container
 * - toSendString to allow easy sending to ConnectionService
 */

public class Msg {
    String sndrMac;
    char rcvrID;
    char reqType;
    char finalState;
    char errorFlags;

    public Msg(String nSndrMac, char nRcvrID, char nReqType, char nFinalState, char nErrorFlags)
    {
        sndrMac = nSndrMac;
        rcvrID = nRcvrID;
        reqType = nReqType;
        finalState = nFinalState;
        errorFlags = nErrorFlags;
    }

    public String toSendString()
    {
        String str = sndrMac;
        str += Character.toString(rcvrID);
        str += Character.toString(reqType);
        str += Character.toString(finalState);
        str += Character.toString(errorFlags);

        return str;
    }

};

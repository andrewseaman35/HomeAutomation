package com.seaman.andrew.homeautomation;

import java.math.BigInteger;
import java.util.BitSet;
/**
 * Class to use as protocol for messages sent between
 *  Android application and Arduino server
 * Created by Andrew on 6/26/2015.
 * Project: Home Automation
 * Version 1.0
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

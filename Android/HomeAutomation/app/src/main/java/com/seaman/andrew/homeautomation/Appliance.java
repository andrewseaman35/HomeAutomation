package com.seaman.andrew.homeautomation;

/**
 * Created by Andrew on 7/15/2015.
 */
public class Appliance {
    String name;
    String type;
    int state;
    int id;
    // todo: add appliance id

    Appliance(String nName, String nType, int nId, int nState)
    {
        this.name = nName;
        this.type = nType;
        this.state = nState;
        this.id = nId;
    }

    char getTypeChar() {
        return this.type.charAt(0);
    }
}

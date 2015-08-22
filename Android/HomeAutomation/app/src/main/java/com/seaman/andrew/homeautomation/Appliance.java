package com.seaman.andrew.homeautomation;

/*
 * File: Appliance.java
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 *
 * Description: This file contains the class declaration of the Appliance
 *  class. This class is used as a structure to hold the required data
 *  for each appliance.
 *
 * Current Progress (incomplete)
 * - Structure created and implemented
 *
 * TODO: make argument-less constructor private, ensure that this is never used
 */
public class Appliance {
    String name;
    String type;
    int state;
    int id;

    Appliance(String nName, String nType, int nId, int nState)
    {
        this.name = nName;
        this.type = nType;
        this.state = nState;
        this.id = nId;
    }
}

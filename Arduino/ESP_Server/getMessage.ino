/*
 * File: getMessage.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the method to parse an array of
 *  characters into a Msg, It checks the length of the array as well
 *  as the MAC address of the sender.
 *  
 * Current Progress (completed)
 * - Message length checked
 * - Sender MAC checked
 * - Msg parsed
 */

Msg getMessageFromChars(char data[]) {
  Msg msg;

  // check to ensure that message is proper length
  if(data[7] != '2' || data[8] != '1') {
    Serial.println("wrong length");
    return msg;
  }
    
  // check to ensure that MAC is accepted
  if(!macIsKnown(data)) {
    Serial.println("unknown mac");
    return msg;
  } 

  // convert all to MSG
  // set MAC
  for(int i = 0; i < 17; i++) {
    msg.sndrMac[i] = data[i+10];
  }

  // set recipient
  msg.rcvrID = data[27];
  // set request
  msg.reqType = data[28];
  // set final state
  msg.finalState = data[29];
  // set errors
  msg.errorFlags = data[30];

  // return
  return msg;
}


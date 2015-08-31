/*
 * File: dumpESP.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains a method to dump all available serial data for the
 *  ESP
 *  
 * Current Progress (complete)
 * - dumps all available data
 */

void dumpESP(){
  char temp;
  
  while(ESP.available()){
    temp = ESP.read();
    delay(1);
  } 
}

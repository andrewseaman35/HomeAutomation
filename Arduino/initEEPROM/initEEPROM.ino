/*
 * File: initEEPROM.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the method to be run before working
 *  with the WiFi or Bluetooth module code. It is used to ensure that 
 *  all of the EEPROM values are set to 255, as this is expected to be 
 *  the default, unwritten value in the other code.
 *  
 *  NOTE: EEPROM writes are limited around 100,000 cycles. 
 *        DO NOT USE UNNECESSARILY
 *  
 * Current Progress (complete)
 *  - Checks all EEPROM values
 *  - Changes values if not default (255)
 *  - Prints out all values to confirm
 */

#include <EEPROM.h>

void setup() {
  Serial.begin(115200);
  for(int i = 0; i < 1024; i++)
  {
    if(EEPROM.read(i) != 255) {
      EEPROM.write(i, 255);
    }
  }

  for(int i = 0; i < 1024; i++) {
    Serial.print(i);
    Serial.print(": ");
    Serial.println(EEPROM.read(i));
  }

}

void loop() {
  // put your main code here, to run repeatedly:

}

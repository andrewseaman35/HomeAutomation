/*
 * File: findResponse.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the method used to find a specific keyword in
 *  received Serial data. It is used to determine whether an AT setup command
 *  is successful. It creates a buffer the length of the desired keyword and checks
 *  it for the keyword. If it is not there, it uses the buffer as a wrap around buffer
 *  and continues to check it for the desired keyword.
 *  
 * Current Progress (complete)
 * - keyword found through buffer
 * - timeout implemented
 */

boolean findResponse(char kWord[], int kLength){
  char data[10];
  long timeoutStart = millis();
  long timeout = 9000; // 9 second timeout

  for(byte i = 0; i < kLength; i++) {
    // wait until data is available to read
    while(!HC_MASTER.available()) {
      if((millis() - timeoutStart) > timeout) {
        Serial.println("Timed out");
        return false;
      }
    }

    data[i] = HC_MASTER.read();
  }

  while(true) {
    for(byte i = 0; i < kLength; i++) {
      if(kWord[i] != data[i]) {
        break;
      }
      if(i == (kLength - 1)) {
        return true;
      }
    }

    for(byte i = 0; i < (kLength -1); i++) {
      data[i] = data[i+1];
    }

    // wait while no data is available
    while(!HC_MASTER.available()) {
      if((millis() - timeoutStart) > timeout) {
        Serial.println("Timed out");
        return false;
      }
    }

    data[kLength - 1] = HC_MASTER.read(); 
  }
}


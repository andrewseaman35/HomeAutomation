/*
 * File: setupHC_SLAVE.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the method to set up the HC-06 module via AT commands.
 *  The HC module is checked and then the name is set. The LED blinks twice when the setup
 *  is started, and quickly flashes off when a set up command is successful.
 *  
 * Current Progress (complete)
 * - Check HC
 * - Set name
 *
 */

boolean setupHC_SLAVE() {
  // initial HC_SLAVE check
  HC_SLAVE.print("AT");

  digitalWrite(errorPin, LOW);
  delay(1000);
  digitalWrite(errorPin, HIGH);
  delay(1000);
  digitalWrite(errorPin, LOW);
  delay(1000);
  digitalWrite(errorPin, HIGH);

  // looking for "OK"
  /*
  if(findResponse("OK", 2))
    Serial.println("HC_SLAVE CHECK OK");
  else
  {
    Serial.println("HC_SLAVE CHECK FAILED");
    return false;
  }
  */
  if(!findResponse("OK")) {
    return false;
  }
  digitalWrite(errorPin, LOW);
  delay(500);
  digitalWrite(errorPin, HIGH);
  dumpHC_SLAVE();

  // change name to input string (THIS MUST BE SET INITIALLY)
  HC_SLAVE.print("AT+NAME00x0");

  // looking for "OK"
  /*
  if(findResponse("OK", 2))
    Serial.println("HC_SLAVE NAME OK");
  else
  {
    Serial.println("HC_SLAVE NAME FAILED");
    return false;
  }*/
  
  if(!findResponse("OK")) {
    return false;
  }
    digitalWrite(errorPin, LOW);
  delay(1000);
  digitalWrite(errorPin, HIGH);
  
  dumpHC_SLAVE();

  return true;
}


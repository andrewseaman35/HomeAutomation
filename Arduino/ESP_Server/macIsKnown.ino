/*
 * File: macIsKnown.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the method that checks the MAC
 *  address to the known address to see if it was received by a 
 *  valid sender.
 *  
 * Current Progress (incomplete)
 * - checks MAC to the only used MAC
 *  - is not secure at all, terrible method for now
 */

bool macIsKnown(char data[]) {
  String knownMac = "a8:06:00:4c:88:07";

  for(int i = 0; i < 17; i++) {
    if(data[i+10] != knownMac[i])
      return false;
  }

  return true;
}

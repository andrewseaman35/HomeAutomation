/*
 * File: dumpHC_MASTER.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains a method to dump all available serial data for the
 *  HC_MASTER
 *  
 * Current Progress (complete)
 * - dumps all available data
 */

void dumpHC_MASTER(){
  char temp;
  
 while(HC_MASTER.available()){
    temp = HC_MASTER.read();
    delay(1);
  }
}

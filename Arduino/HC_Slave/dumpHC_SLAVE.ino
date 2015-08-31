/*
 * File: dumpHC_SLAVE.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains a method to dump all available serial data for the
 *  HC_Slave
 *  
 * Current Progress (complete)
 * - dumps all available data
 */

void dumpHC_SLAVE(){
  char temp;
  
 while(HC_SLAVE.available()){
    temp = HC_SLAVE.read();
    delay(1);
  }
}

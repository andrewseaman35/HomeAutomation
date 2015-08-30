boolean findResponse(char kWord[]){
  timeoutStart = millis();
  int kLength = 2;

  for(byte i = 0; i < kLength; i++) {
    // wait until data is available to read
    while(!HC_SLAVE.available()) {
      if((millis() - timeoutStart) > timeout) {
        //Serial.println("Timed out");
        return false;
      }
    }

    responseData[i] = HC_SLAVE.read();
  }

  while(true) {
    for(byte i = 0; i < kLength; i++) {
      if(kWord[i] != responseData[i]) {
        break;
      }
      if(i == (kLength - 1)) {
        return true;
      }
    }

    for(byte i = 0; i < (kLength -1); i++) {
      responseData[i] = responseData[i+1];
    }

    // wait while no data is available
    while(!HC_SLAVE.available()) {
      if((millis() - timeoutStart) > timeout) {
        //erial.println("Timed out");
        return false;
      }
    }

    responseData[kLength - 1] = HC_SLAVE.read(); 
  }
}


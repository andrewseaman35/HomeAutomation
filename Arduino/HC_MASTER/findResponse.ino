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


boolean readFromESP(const char keyword[], int key_size, int timeout_val, byte mode) {
  int timeout_start = millis();
  char dataIn[20]; // length of max keyword = 20

  for(byte i = 0; i < key_size; i++) {
    while(!ESP8266.available()) {
      if((millis() - timeout_start) > timeout_val) { 
        Serial.println("timed out");
        return false;
      }
    }

    dataIn[i] = ESP8266.read();
  }

  return true;
}


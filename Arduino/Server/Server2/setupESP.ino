void setupESP() {
  Serial.print("Connecting to server...");
  ESP8266.write("AT+CWJAP=\"ATT564\",\"4182103725\"");
  while(!ESP8266.available()) {
    Serial.print(".");
    delay(100);
    }
  while(ESP8266.available()) {
    Serial.print(ESP8266.read());
  }
  delay(1000);

  ESP8266.write("AT+CWMODE=1");

  while(ESP8266.available()) {
    Serial.print(ESP8266.read());
  }
  delay(1000);
}


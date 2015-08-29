boolean setupESP(){
  
  ESP.print("AT\r\n");
  
  if(findResponse("OK", 2)) {
    Serial.println("ESP CHECK OK");
  } else {
    Serial.println("ESP CHECK FAILED");
    return false;
  }
  
  dumpESP();

  ESP.print("AT+RST\r\n");
  
  if(findResponse("OK", 2)) {
    Serial.println("ESP RESET OK");
  } else { 
    Serial.println("ESP RESET FAILED"); 
    return false;
  }

  dumpESP();
  
  ESP.print("AT+CWMODE=");
  ESP.print(CWMODE);
  ESP.print("\r\n");
  
  if(findResponse("OK", 2)) {
    Serial.println("ESP CWMODE SET");
  } else {
    Serial.println("ESP CWMODE SET FAILED");
    return false;
  }
  
  delay(2000);
  dumpESP();  
   
  ESP.print("AT+CWJAP=\"");
  ESP.print(SSID_NAME);
  ESP.print("\",\"");
  ESP.print(SSID_PASS);
  ESP.print("\"\r\n");
   
  if(findResponse("OK", 2)) {
    Serial.println("ESP SSID SET OK");
  } else {
    Serial.println("ESP SSID SET FAILED");   
    return false;
  }
  
  dumpESP();
  
  ESP.print("AT+CIPMUX=");// set the CIPMUX
  ESP.print(CIPMUX);//from constant
  ESP.print("\r\n");
  
  if(findResponse("OK", 2)) {
    Serial.println("ESP CIPMUX SET");
  } else {
    Serial.println("ESP CIPMUX SET FAILED"); 
    return false;
  }
  
  dumpESP();

  ESP.print("AT+CIPSERVER=");
  ESP.print(SERVER_MODE);
  ESP.print(",");
  ESP.print(SERVER_PORT);
  ESP.print("\r\n");
  
  if(findResponse("OK", 2)) {
    Serial.println("ESP SERVER SET");
  } else {
    Serial.println("ESP SERVER SET FAILED"); 
    return false;
  }
  
  dumpESP();
 
  return true;
  
}

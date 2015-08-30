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


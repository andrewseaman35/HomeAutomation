boolean setupHC(String name) {
  // initial HC check
  HC.print("AT");

  // looking for "OK"
  if(findResponse("OK", 2))
    Serial.println("HC CHECK OK");
  else
  {
    Serial.println("HC CHECK FAILED");
    return false;
  }
  
  dumpHC();

  // change name to input string (THIS MUST BE SET INITIALLY)
  HC.print("AT+NAME");
  HC.print(name);

  // looking for "OK"
  if(findResponse("OK", 2))
    Serial.println("HC NAME OK");
  else
  {
    Serial.println("HC NAME FAILED");
    return false;
  }
  
  dumpHC();

  return true;
}


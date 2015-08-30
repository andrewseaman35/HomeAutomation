boolean setupHC_MASTER() {
  // initial HC check
  HC_MASTER.print("AT\r\n");

  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER CHECK OK");
  } else {
    Serial.println("HC_MASTER CHECK FAILED");
    return false;
  }

  dumpHC_MASTER();

  // remove all known addresses
    HC_MASTER.print("AT+RMAAD\r\n");

  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER RMAAD OK");
  } else {
    Serial.println("HC_MASTER RMAAD FAILED");
    return false;
  }

  dumpHC_MASTER();

  // set communcation baud rate
  HC_MASTER.print("AT+UART=4800,0,0\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER SET BAUD OK");
  } else {
    Serial.println("HC_MASTER SET BAUD FAILED");
    return false;
  }

  dumpHC_MASTER();

  // set password
  HC_MASTER.print("AT+PSWD=3593\r\n");

  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER SET PASSWORD OK");
  } else {
    Serial.println("HC_MASTER SET PASSWORD FAILED");
    return false;
  }

  dumpHC_MASTER();

  // set as master
  HC_MASTER.print("AT+ROLE=1\r\n");

  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER SET MASTER OK");
  } else {
    Serial.println("HC_MASTER SET MASTER FAILED");
    return false;
  }

  dumpHC_MASTER();

  // set connection mode to connect to specified
  // 0 = connect to specified Bluetooth address
  // 1 = connect to any address
  HC_MASTER.print("AT+CMODE=0\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC_MASTER CMODE SET");
  } else {
    Serial.println("HC_MASTER CMODE SET FAILED");
    return false;
  }

  dumpHC_MASTER();

    // inqm
  HC_MASTER.print("AT+INQM=0,5,7\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC INQM SET");
  } else {
    Serial.println("HC INQM FAILED");
    return false;
  }

  dumpHC_MASTER();

  // init
  HC_MASTER.print("AT+INIT\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC INIT SET");
  } else {
    Serial.println("HC INIT FAILED");
  }

  dumpHC_MASTER();

  // inquiry
  HC_MASTER.print("AT+INQ\r\n");
  
  // looking for "OK"
  if(findResponse("INQ", 3)) {
    Serial.println("HC INQ SUCCESS");
  } else {
    Serial.println("HC INQ FAILED");
  }

  dumpHC_MASTER();

  // pair
  HC_MASTER.print("AT+PAIR=2015,5,75321,9\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC PAIR SET");
  } else {
    Serial.println("HC PAIR FAILED");
    return false;
  }

  dumpHC_MASTER();

  // bind
  HC_MASTER.print("AT+BIND=2015,5,75321\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC BIND SET");
  } else {
    Serial.println("HC BIND FAILED");
    return false;
    }

  dumpHC_MASTER();

    HC_MASTER.print("AT+CMODE=1\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC CMODE SET");
  } else {
    Serial.println("HC CMODE SET FAILED");
    return false;
  }

  dumpHC_MASTER();

    // bind
  HC_MASTER.print("AT+LINK=2015,5,75321\r\n");
  
  // looking for "OK"
  if(findResponse("OK", 2)) {
    Serial.println("HC LINK SET");
  } else {
    Serial.println("HC LINK FAILED");
    return false;
  }

  dumpHC_MASTER();
  return true;

}

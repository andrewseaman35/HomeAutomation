boolean setup_HC(String name) {
  HC.write("AT\r\n");
  if(read_until_HC(keyword_OK,sizeof(keyword_OK),5000,0))//go look for keyword "OK" with a 5sec timeout
    Serial.println("HC CHECK OK");
  else
  {
    Serial.println("HC CHECK FAILED");
    return false;
  }
  serial_dump_HC();//this just reads everything in the buffer and what's still coming from the ESP

  HC.print("AT");
  HC.print(name);
  HC.print("\r\n");
  if(read_until_HC(keyword_OK,sizeof(keyword_OK),5000,0))//go look for keyword "OK" with a 5sec timeout
    Serial.println("HC NAME OK");
  else
  {
    Serial.println("HC NAME FAILED");
    return false;
  }
  serial_dump_HC();

  return true;
}


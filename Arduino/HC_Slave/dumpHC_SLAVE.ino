void dumpHC_SLAVE(){
  char temp;
  
 while(HC_SLAVE.available()){
    temp = HC_SLAVE.read();
    delay(1);
  }
}

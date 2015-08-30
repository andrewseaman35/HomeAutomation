void dumpHC_MASTER(){
  char temp;
  
 while(HC_MASTER.available()){
    temp = HC_MASTER.read();
    delay(1);
  }
}

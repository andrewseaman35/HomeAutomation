void dumpHC(){
  char temp;
  
 while(HC.available()){
    temp = HC.read();
    delay(1);
  }
}

bool macIsKnown(char data[]) {
  String knownMac = "a8:06:00:4c:88:07";

  for(int i = 0; i < 17; i++) {
    if(data[i+10] != knownMac[i])
      return false;
  }

  return true;
}

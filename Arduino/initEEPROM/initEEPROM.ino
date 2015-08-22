#include <EEPROM.h>

void setup() {
  Serial.begin(115200);
  for(int i = 0; i < 1024; i++)
  {
    if(EEPROM.read(i) != 255) {
      EEPROM.write(i, 255);
    }
  }

  for(int i = 0; i < 1024; i++) {
    Serial.print(i);
    Serial.print(": ");
    Serial.println(EEPROM.read(i));
  }

}

void loop() {
  // put your main code here, to run repeatedly:

}

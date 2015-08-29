#include <SoftwareSerial.h>

SoftwareSerial HC(2, 3); // RX, TX

char data[3]; // holds 3 to represent length of BTMsg

void setup()
{
    Serial.begin(9600);
    HC.begin(4800);

    // keep trying to set up HC
    while(!setupHC("00x0")) {}

    Serial.println("Setup complete");
}

void loop()
{
  char data;

/*
 * while(!HC.available()) {}
 * 
 * for(byte i = 0; i < 3; i++) {
 *  data[i] = HC.read();
 *  while(!HC.available()) {}
 * }
 */
    while (HC.available() )
    { 
        data = HC.read();
        Serial.print(data);
    }

    while ( Serial.available() )
    { 
        data = Serial.read();
        HC.print(data);
    }
}

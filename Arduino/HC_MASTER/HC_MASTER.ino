#include <SoftwareSerial.h>

// PIN NUMBERS PROBABLY HAVE TO CHANGE FOR ATMEGA
#define errorPin 6

SoftwareSerial HC_MASTER(9, 8); // RX, TX

char msg[3]; // holds 3 to represent length of BTMsg
  // msg[0] = applianceID
  // msg[1] = type
  // msg[2] = final state

void setup()
{
    Serial.begin(9600);
    HC_MASTER.begin(38400);

    pinMode(errorPin, OUTPUT);

    // illuminate errorPin until HC has been set up properly
    digitalWrite(errorPin, HIGH);

    // keep trying to set up HC
    while(!setupHC_MASTER()) {}


    
    Serial.println("Serial Baud set");

    HC_MASTER.flush();
    delay(2);
    HC_MASTER.end();
    HC_MASTER.begin(4800);

    digitalWrite(errorPin, LOW);
    Serial.println("Setup complete");
}

void loop()
{
  delay(5000);
  HC_MASTER.print(0);
  HC_MASTER.println(71);
  HC_MASTER.print(72);
  Serial.println("ON sent\n");
  delay(5000);
  HC_MASTER.print('x');
  HC_MASTER.print('t');
  HC_MASTER.print('0');
  Serial.println("OFF sent\n");
}

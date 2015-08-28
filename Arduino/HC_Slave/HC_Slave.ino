#include <SoftwareSerial.h>

#define HC_rxPin 2 // these will change for ATTiny
#define HC_txPin 3
#define relayPin 4;

SoftwareSerial HC(HC_rxPin, HC_txPin);

// HC-06 MODULE MUST BE MANUALLY SET UP BEFORE RUNNING THIS
// BAUD = 9600
// NAME = (ATMEGA)x(0-6) ex. 09x3

void setup() {
  pinMode(relayPin, OUTPUT);
  
  HC.begin(9600); // BAUD RATES MUST BE CHANGED BEFOREHAND
  HC.listen();
  Serial.begin(115200);

  delay(3000); // three second pause before running

  // dump all available data at HC serial input
  char temp;
  while(HC.available()) {
    temp = HC.read();
    delay(5);
  }
  
}

void loop() {
  char data[2];

  if(HC.available()) {
    for(byte i = 0; i < 3; i++) {
      data[i] = HC.read();
      while(!ESP.available()) {}
    }
  }

  if(data[0] == 't') { // toggle type appliance
    if(data[1] == '1' { // turn on
      digitalWrite(relayPin, HIGH);
    } else if(data[1] == '0') { // turn off
      digitalWrite(relayPin, LOW);
    }
  }
  
  
}

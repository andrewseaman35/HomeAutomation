#include <SoftwareSerial.h>

#define relayIn 1
#define relayOut 0
#define errorPin 4

SoftwareSerial HC_SLAVE(2, 3); // RX, TX

int rcvr, type, state;

char responseData[8];
long timeoutStart;
long timeout = 7000;
  
void setup()
{
    Serial.begin(9600);
    HC_SLAVE.begin(4800);
    HC_SLAVE.listen();

    // relayIn will be used to check the status of the relay
    pinMode(relayIn, INPUT);

    // relayOut will be used to alter the status of the relay
    pinMode(relayOut, OUTPUT);

    pinMode(errorPin, OUTPUT);

    // illuminate errorPin until HC_SLAVE has been set up properly
    digitalWrite(errorPin, HIGH);

    // keep trying to set up HC_SLAVE
    while(!setupHC_SLAVE()) {}

    digitalWrite(errorPin, LOW);

    Serial.println("Setup complete");
}

void loop()
{

  while(true) {
    while(!HC_SLAVE.available()) {}
    
    type = HC_SLAVE.read();
    Serial.print("->");
    Serial.print(type);
    Serial.print("<-");
  }
  /*
  while(!HC_SLAVE.available()) {
    digitalWrite(errorPin, HIGH);
    delay(150);
    digitalWrite(errorPin, LOW);
    delay(150);
    }

  /*for(byte i = 0; i < 3; i++) {
    msg[i] = HC_SLAVE.read(); 
    while(!HC_SLAVE.available()) {}
  }*/
/*
  dumpHC_SLAVE();

  rcvr = HC_SLAVE.read();
  while(!HC_SLAVE.available()) {}
  type = HC_SLAVE.read();
  while(!HC_SLAVE.available()) {}
  state = HC_SLAVE.read();

  Serial.print("rcvr: ");
  Serial.print(rcvr);
  Serial.print("\ntype: ");
  Serial.print(type);
  Serial.print("\nstate: ");
  Serial.print(state);
*/
/*
  if(rcvr == 'x') {
    digitalWrite(relayOut, HIGH);
  } else {
    digitalWrite(relayOut, HIGH);
    delay(5000);
    digitalWrite(relayOut, LOW);
  }
 */
}

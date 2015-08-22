#include "ESP8266WiFi.h"
#include "ESP8266WebServer.h"
#include "ESP8266mDNS.h"

class Msg{
  public:
    char sndrMac[17];
    char rcvrID;
    char reqType;
    char finalState;
    char errorFlags;
    
    void getMsgFromChars(char charArray[]){
      for(int i = 0; i < 17; i++) {
        this->sndrMac[i] = charArray[i];
      }
      this->rcvrID = charArray[17];
      this->reqType = charArray[18];
      this->finalState = charArray[19];
      this->errorFlags = charArray[20];
    }
    void printMsg() {
      Serial.print("MAC: ");
      for(int i = 0; i < 17; i++) {
        Serial.print(this->sndrMac[i]);
      }
      Serial.println("");
      Serial.print("rcvrID: ");
      Serial.println(this->rcvrID);
      Serial.print("reqType: ");
      Serial.println(this->reqType);
      Serial.print("finalState: ");
      Serial.println(this->finalState);
      Serial.print("errorFlags: ");
      Serial.println(this->errorFlags);
    }
      
  };

// Network constants
const int PORT = 36330;
const char SSID[] = "ATT564";
const char PASSWORD[] = "4182103725";
const int CONNECT_TIMEOUT= 30;

const int MSG_SIZE = 21;
const int BAUD_RATE = 9600;

// Error codes
const int ERR_NETWORK_UNAVAILABLE = 1;

// Pins
const int PIN_NO_WIFI = 2;

int loopCounter = 0;

WiFiServer server(PORT);

void setup() {
  // pin setup
  pinMode(PIN_NO_WIFI, OUTPUT);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  
  Serial.begin(BAUD_RATE);

  // WiFi setup
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);
  Serial.println("Setup done");

  Serial.print("Attempting to connect to ");
  Serial.println(SSID);
  WiFi.begin(SSID, PASSWORD);
  Serial.println("Timeout in\n   ");
  
  int timeoutCount = CONNECT_TIMEOUT;
  while(WiFi.status() != WL_CONNECTED && timeoutCount > 0) {
    Serial.print(timeoutCount);
    delay(400);
    Serial.print(" .");
    delay(200);
    Serial.print(".");
    delay(200);
    Serial.print(". ");
    delay(200);
    --timeoutCount;
  }

  if(WiFi.status() == WL_CONNECTED) {
    Serial.println("\nConnected!");
    server.begin();
    Serial.print("Server started on port ");
    Serial.println(PORT);
    Serial.print("Server IP: ");
    Serial.println(WiFi.localIP());
  } else {
    Serial.println("Could not connect to WiFi");
    WiFi.disconnect();
    
    while(true) {
      digitalWrite(PIN_NO_WIFI, HIGH);
      delay(1000);
      digitalWrite(PIN_NO_WIFI, LOW);
      delay(1000);
    }
  }
}

void loop() {
  loopCounter = (loopCounter + 1) % 100000;
  if(loopCounter == 0)
  {
    Serial.println("Running...");
  } 
  WiFiClient client = server.available();
  Msg message;
  char msgChars[MSG_SIZE];
  if(client) {
    while(client.connected()) {
      if(client.available()) {
        for(int i = 0; i < MSG_SIZE; i++)
        {
          msgChars[i] = client.read();
        }
        message.getMsgFromChars(msgChars);
        message.printMsg();

        toggleLED((int)message.rcvrID);
      }
    }
  }
  client.stop();
}

void toggleLED(int pin)
{
  if(digitalRead(pin) == HIGH) {
    digitalWrite(pin, LOW);    
  } else {
    digitalWrite(pin, HIGH);
  }
}

int findNetworks()
{
  Serial.println("Scanning for available networks...");

  int networkCount = WiFi.scanNetworks();

  Serial.print("Found ");
  Serial.print(networkCount);
  Serial.print(" networks:\n");
  
  for(int i = 0; i < networkCount; i++) {
    Serial.print("  ");
    Serial.println(WiFi.SSID(i));
  }

  return networkCount;
}







#include <SoftwareSerial.h>

#define ESP8266_rxPin 4
#define ESP8266_txPin 5

boolean read_until_ESP(const char keyword1[], int key_size, int timeout_val, byte mode);

const char SSID_ESP[] = "ATT564";
const char SSID_PASS[] = "4182103725";

const char CWMODE = '1'; // station mode
const char CIPMUX = '1'; // multi connect

SoftwareSerial ESP8266(ESP8266_rxPin, ESP8266_txPin);

//DEFINE KEYWORDS HERE
const char keyword_OK[] = "OK";
const char keyword_Ready[] = "Ready";
const char keyword_no_change[] = "no change";
const char keyword_blank[] = "#&";
const char keyword_ip[] = "192.";
const char keyword_rn[] = "\r\n";
const char keyword_quote[] = "\"";
const char keyword_carrot[] = ">";
const char keyword_sendok[] = "SEND OK";
const char keyword_linkdisc[] = "Unlink";

// DEFINE FUNCTIONS HERE
boolean readFromESP(const char keyword1[], int key_size, int timeout_val, byte mode);
void setupESP();

void setup() {
    //Pin Modes for ESP TX/RX
  pinMode(ESP8266_rxPin, INPUT);
  pinMode(ESP8266_txPin, OUTPUT);
  
  ESP8266.begin(9600);//default baudrate for ESP
  ESP8266.listen();//not needed unless using other software serial instances
  Serial.begin(115200); //for status and debug
  delay(2000);//delay before kicking things off

  setupESP();

}

void loop() {


}

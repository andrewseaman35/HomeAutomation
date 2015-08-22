/*
 * File: ESP_Server.ino
 * Project: Home Automation
 * Author: Andrew Seaman
 * Date Modified: August 21, 2015
 * 
 * Description: This file contains the main part of the Arduino-run server.
 *  It will run on the boot of the Arduino and will start the server for the
 *  individual WiFi hub. In addition, it will open and check the Bluetooth
 *  connections to the inidividual relay systems controlled by the ATTinies
 *  over the HC-05. This server will accept messages from the Android
 *  application via the Msg message format over a TCP connection. The MAC
 *  address of the device is checked and if valid, the message is parsed.
 *  A simplified form of the message will then be sent over Bluetooth to the
 *  according Bluetooth-relay system.
 *  
 * Current Progress (incomplete)
 * - ESP is set up
 *   - ESP checked
 *   - Server begun on specified port
 * - Loop allows TCP client to connect
 * - Msg parsed from received bytes
 */

#include <Msg.h>

#include <SoftwareSerial.h>
#include <EEPROM.h>

#define ESP_rxPin 4 // Connects to TX on ESP8266
#define ESP_txPin 5 // Connects to RX on ESP8266

//NETWORK CONSTANTS
const char SSID_ESP[] = "ATT564";
const char SSID_KEY[] = "4182103725";
const char SERVER_MODE = '1'; //1=Open, 2=Close
const char SERVER_PORT[] = "36330";

//MODES
const char CWMODE = '1'; //CWMODE 1=STATION, 2=APMODE, 3=BOTH
const char CIPMUX = '1'; //CIPMUX 0=Single Connection, 1=Multiple Connections

SoftwareSerial ESP(ESP_rxPin, ESP_txPin); // RX, TX on Arduino

//DEFINE ALL FUNCTIONS HERE
boolean setup_ESP();
boolean read_until_ESP(const char keyword1[], int key_size, int timeout_val, byte mode);
void timeout_start();
boolean timeout_check(int timeout_ms);
void serial_dump_ESP();
boolean connect_ESP();
Msg getMessageFromChars(char data[]);
boolean macIsKnown(char data[]);

//DEFINE ALL GLOBAL VARIABLES HERE
unsigned long timeout_start_val;
char scratch_data_from_ESP[20];//first byte is the length of bytes
char payload[150];
byte payload_size=0, counter=0;
char ip_address[16];
char dataIn[100];

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

//DEFINE EEPROM INDICES HERE
const int EEPROM_DEVICE_ID = 0;
const int EEPROM_ERROR = 1022;
const int EEPROM_HAS_BEEN_INIT = 1023;

void setup(){
  //Pin Modes for ESP TX/RX
  pinMode(ESP_rxPin, INPUT);
  pinMode(ESP_txPin, OUTPUT);
  
  ESP.begin(9600);//default baudrate for ESP
  ESP.listen();
  Serial.begin(115200); //for status and debug

  // Take three second pause before set up
  for(byte i = 3; i > 0; i--)
  {
    delay(1000);
    Serial.print(i);
    Serial.print("... ");
  }
  Serial.println();

  // Keep trying to perform set up
  while(setup_ESP() == false)
  {}

  serial_dump_ESP();
}

void loop(){
  Msg msg;
  
  if(ESP.available()) {
    // desired message
    // +IPD,0,21:a8:06:00:4c:88:07Tae
    // +IPD,ID,LEN:MAC_OF_SENDER 'rcvrID''Type''a''e'
    while((dataIn[0] = ESP.read()) != '+') {
      // this makes dataIn[0] the + before IPD
      while(!ESP.available())
      {}
    }
    while(!ESP.available())
    {}
    // read the next 21 bytes, pausing if no more available
    for(byte i = 1; i < 32; i++) {
      dataIn[i] = ESP.read();
      while(!ESP.available())
      {}
    }
    
    msg = getMessageFromChars(dataIn);
    msg.printMsg(); 
  }


}

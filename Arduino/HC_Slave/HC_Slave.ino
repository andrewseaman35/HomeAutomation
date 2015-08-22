#include <SoftwareSerial.h>

#define HC_rxPin 2 // these will change for ATTiny
#define HC_txPin 3

SoftwareSerial HC(10, 11);

// DEFINE ALL FUNCTIONS HERE
boolean setup_HC(String name);
boolean read_until_HC(const char keyword1[], int key_size, int timeout_val, byte mode);
void serial_dump_HC();

//DEFINE ALL GLOBAL VARIABLES HERE
unsigned long timeout_start_val;
char scratch_data_from_HC[20];//first byte is the length of bytes
char payload[150];
byte payload_size=0, counter=0;
char dataIn[100];

// DEFINE KEYWORDS HERE
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


void setup() {
  String name = "00x0"; // THIS MUST BE CONFIGURED FOR EACH ATTINY
  
  //pinMode(HC_rxPin, INPUT);
  //pinMode(HC_txPin, OUTPUT);

  HC.begin(9600); // BAUD RATES MUST BE CHANGED BEFOREHAND
  HC.listen();
  Serial.begin(115200);

    // Take three second pause before set up
  for(byte i = 3; i > 0; i--)
  {
    delay(1000);
    Serial.print(i);
    Serial.print("... ");
  }
  Serial.println();

  HC.print("AT");
  delay(1000);
  HC.print("AT+VERSION");
  delay(1000);
  

  HC.print("AT");
  while(!HC.available()) {
    Serial.println("...");
    delay(500);
  }
  Serial.print(HC.read());
  
  // Keep trying to perform set up
  while(setup_HC(name) == false)
  {}

  serial_dump_HC();

}

void loop() {
  // put your main code here, to run repeatedly:

}

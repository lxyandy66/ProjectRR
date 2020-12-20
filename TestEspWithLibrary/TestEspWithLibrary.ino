#include <ArduinoJson.h>
#include <DHT.h>
#include "../DevBoardESP8266.h"


/*
  Software serial multple serial test

 Receives from the hardware serial, sends to software serial.
 Receives from software serial, sends to hardware serial.

 The circuit:
 * RX is digital pin 10 (connect to TX of other device)
 * TX is digital pin 11 (connect to RX of other device)

 created back in the mists of time
 modified 25 May 2012
 by Tom Igoe
 based on Mikal Hart's example

 This example code is in the public domain.

 */
#include <SoftwareSerial.h>
#include "DevBoardESP8266.h"
#include"Agent.h"

#define ESP_SSID  "I am AB, How R U?"//"TP-LINK_hvac" "BlackBerry Hotspot"
#define ESP_PASS  "woyeshiab"         // Your network password here "141242343"

#define TCP_SERVER_ADDR "192.168.1.104" //TCP服务器地址
#define TCP_SERVER_PORT 8266            //TCP服务器地址
#define led D2

SoftwareSerial mySerial(10, 11); // RX, TX10, 11
DevBoardESP8266 wifi(&mySerial, &Serial, D3);
const String deviceID = "stm32";
boolean ledOn = false;
StaticJsonDocument<256> jsonOut;
#define PIN_DHT_DATA A1
#define DHT_TYPE DHT22
DHT dht22(PIN_DHT_DATA, DHT_TYPE);

String tempBuffer;

Agent agent("hello");

void setup()
{
  char buffer[50];
  pinMode(led, OUTPUT);
  pinMode(D3, OUTPUT);

  // Open serial communications and wait for port to open:
  //USB串口测试
  dht22.begin();
  Serial.begin(9600);
  while (!Serial)
  {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  Serial.println("Goodnight moon!");

  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);
  while (!mySerial)
  {
    ;
  }
  Serial.println("SS initialized!");
  wifi.hardReset();

  //Wi-Fi连接测试
  Serial.println(F("Connecting to WiFi..."));
  boolean flag = wifi.connectToAP(F(ESP_SSID), F(ESP_PASS));

  if (flag)
  {
    Serial.println("Connecting Success");
  }
  else
  {
    Serial.println("Connecting Failed");
  }

  wifi.connectTCP(F(TCP_SERVER_ADDR), TCP_SERVER_PORT);

  Serial.println(F("Setup finished"));
}

void loop()
{ // run over and over
  if (mySerial.available())
  {
    String a = mySerial.readString();
    Serial.println("this is a msg: [" + a + "]");
    // Serial.write(mySerial.read().toCharArray());
    agent.parseMsg(a);
    // processCmd(a);
    return;
  }
  if (Serial.available())
  {
    mySerial.write(Serial.read());
  }

  agent.setWifiModule(wifi);
}

void getIpAddress()
{
  // IP addr check isn't part of library yet, but
  // we can manually request and place in a string.
  char buffer[50];
  wifi.println(F("AT+CIFSR"));
  if (wifi.readLine(buffer, sizeof(buffer)))
  {
    Serial.println(buffer);
    wifi.find(); // Discard the 'OK' that follows
  }
  else
  { // IP addr check failed
    Serial.println(F("error"));
  }
}

void processCmd(String cmd)
{
  Serial.println("CMD: " + cmd);
  if (cmd.indexOf("SET+LED") != -1)
  {
    digitalWrite(led, !ledOn);
    ledOn = !ledOn;
    Serial.println("Led changed");
    return;
  }
  if (cmd.indexOf("GET+TH") != -1)
  {
    Serial.println("in TH measure");
    jsonOut["id"] = deviceID;
    jsonOut["temp"] = String(dht22.readTemperature(), 2);
    jsonOut["Hum"] = String(dht22.readHumidity(), 2);
    serializeJson(jsonOut, tempBuffer);
    jsonOut.clear();
    wifi.sendContent(tempBuffer);
    tempBuffer = "";
    Serial.println("TH measured");
    return;
  }
}



// wifi.println(F("AT+GMR"));
// if (wifi.readLine(buffer, sizeof(buffer))) {
//   Serial.println(buffer);
//   wifi.find(); // Discard the 'OK' that follows
//   Serial.println(F("finished firmware check"));
// }
// else {
//   Serial.println(F("error"));
// }

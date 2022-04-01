
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
 
#define LED 2
 
//Enter your wifi credentials
const char* ssid = "MY PlayFi";  
const char* password =  "MYPlayFi";
 bool PCS=false;
//Enter your mqtt server configurations
const char* mqttServer = "soldier.cloudmqtt.com";    //Enter Your mqttServer address
const int mqttPort = 17816;       //Port number
const char* mqttUser = "bwjmonhj"; //User
const char* mqttPassword = "fKKY2bpfZiZ2"; //Password
int period = 120000;
unsigned long time_now = 0;
WiFiClient espClient;
PubSubClient client(espClient);
int light=0;
 
int Status=-1;
void setup() {
  delay(1000);
 Serial.begin(115200);
  WiFi.begin(ssid, password);
  setPins();
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi..");
  }
  Serial.print("Connected to WiFi :");
  Serial.println(WiFi.SSID());
  
 
  client.setServer(mqttServer, mqttPort);
  client.setCallback(MQTTcallback);
 
  while (!client.connected()) {
    Serial.println("Connecting to MQTT...");
 
    connectMQTT();
  }
 
  
}
void setPins()
{
  pinMode(12,OUTPUT);
  pinMode(13,OUTPUT);
  pinMode(14,OUTPUT);
  pinMode(15,OUTPUT);
  digitalWrite(12,HIGH);
  digitalWrite(13,HIGH);
  digitalWrite(14,HIGH);
  digitalWrite(15,HIGH);
  pinMode(5,OUTPUT);
}
void connectMQTT()
{
  if (client.connect("ESP8266", mqttUser, mqttPassword )) 
  {
 
      Serial.println("connected");  
      String msg="PlayOT Online";
    char msgc[msg.length()];
    msg.toCharArray(msgc,msg.length()+1);
    client.subscribe("test");
 
    client.publish("test",msgc);
    } 
    else {
 
      Serial.print("failed with state ");
      Serial.println(client.state());  //If you get state 5: mismatch in configuration
      delay(2000);
 
    }
    Serial.println(PCS);
}
void MQTTcallback(char* topic, byte* payload, unsigned int length)
{ 
  Serial.print("Message arrived in topic: ");
  Serial.println(topic);
  Serial.print("Message:");
  String message;
  for (int i = 0; i < length; i++) 
  {
    message = message + (char)payload[i];  //Conver *byte to String
  }
  Serial.print(message);
  if(message=="PlayPC Online")
  {
    Status=true;      PCS=true;
  }
  else if(message=="PCOFF")
  {
    Status=false;    
  }
  else if(message=="ON")
  {
    digitalWrite(12,LOW);
    delay(2000);
    Serial.println(PCS);
    if(PCS==false)
    {
      PCS=true;  
      digitalWrite(5,HIGH);
      delay(1000);
      digitalWrite(5,LOW);
    }
  }
  else if(message=="OFF")
  {
    time_now=0;
    if(PCS==true)
    {
           PCS=false;
      delay(2000);
      digitalWrite(5,HIGH);
      delay(1000);
      digitalWrite(5,LOW); 
    } 
      time_now = millis();
  }
  else  if(message=="LO")
  {
    if(light==0)
    {
      light =1;
      digitalWrite(14,LOW);
    }
    else
    {
      light=0;
      digitalWrite(14,HIGH);
    }
  }
  else if(message=="LF")
  {   
    
  }
  else if(message=="PON")
  {
    digitalWrite(12,LOW);
  }
  else if(message=="DOFF"&&Status<1)
  {
    PCS=false;
    digitalWrite(12,HIGH);
  }
  else if(message == "SPOFF")
  {
    PCS=true;
  }
    Serial.println(PCS);
  Serial.println();
  Serial.println("-----------------------");  
}
 
void loop() {
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println(WiFi.reconnect());
    delay(5000);
  }  
    
  if (!client.connected()){Serial.println("Dis");connectMQTT();}
  else client.loop();
  while (Serial.available() > 0) 
  {
    String msg=Serial.readString();
    char msgc[msg.length()];
    msg.toCharArray(msgc,msg.length()+1);
    
  client.publish("test",msgc);
  }
  if(Status<1&&PCS==false&&millis() > time_now + period){
        time_now = millis();
        digitalWrite(12,HIGH);
        Serial.println("Hello");
    }
  }

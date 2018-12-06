﻿
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"
#include "List.h"

MeBuzzer buzzer = MeBuzzer();
String myString;


struct InfoZaAndroid {
	String PrednjiUZSenzor = "PSnz:";
	String DesniUZSenzor = "DSnz:";
	String LijeviUZSenzor = "LSnz:";
	String OcitajSenzorCrte = "RdSnz";
	String ZadnjiInfo = "Over:";
	String Null = "Null:";
};

struct NaredbaAndroida {
	String KreniNaprijed = "RunMotors";
	String ZaustaviSe = "StopMotors";
	String RotirajSeLijevo = "RotateLeft";
	String RotirajSeDesno = "RotateRight";
	String UTurn = "RotateFull";
	String UbrzajLijeviMotor = "SpeedUpLeft";
	String UbrzajDesniMotor = "SpeedUpRight";
	String ZadnjaNaredba = "Over";
	String Null = "Null";
};


struct objektPrimljenePoruke {
	String sadrzaj;
};

objektPrimljenePoruke* a = new objektPrimljenePoruke[];

NaredbaAndroida naredba;

uint16_t brzinaKretanja = 127;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();


uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;
List robotek;

void setup() 
{

  button.setpin(A7);

  bluetooth.begin(115200);
  bluetooth.setTimeout(25);

  //Kreiranje liste
  robotek = List();
  
  a->sadrzaj += "Poruka1";
  a->sadrzaj += "Poruka2";
  a->sadrzaj += "Poruka3";
  a->sadrzaj += "Poruka4";

  a[0].sadrzaj = "lol";
}


void loop() 
{
  IzvrsiPritisakTipke();

  switch (modRadnje)
  {
  case 0:
	  if (a) {
		  buzzer.tone(700, 500);
		  a = false;
	  }
	
	CitajBluetooth();
	IzvrsiRadnjuBT();
	
    break;
  case 1:
    ZaustaviMotore();
  default:
    ZaustaviMotore();
    break;
  }

}

void IzvrsiPritisakTipke()
{
  if (button.pressed() && stisnutGumb)
  {
    stisnutGumb = false;
    modRadnje++;

    if (modRadnje>1)
      modRadnje = 0;
  }
  if (!button.pressed() && !stisnutGumb)
  {
    stisnutGumb = true;
  }
}



void CitajBluetooth()
{
	myString = "";
	  if (bluetooth.available() > 0)
	  {
		  while (bluetooth.read() > 0)
		  {
			  myString = bluetooth.readString();

		  }
	   }

	  if (myString.length() > 0)
	  {
		  char *mejmun = (char*)myString.c_str(); //pretvorba stringa u char
		  if ((mejmun != NULL) && (mejmun[0] != '\0'))
		  {
			  robotek.AddNode(mejmun);
		  }

	  }
}

void IzvrsiRadnjuBT()
{
	int indexZadnjegEl = robotek.brojElemenata() - 1;

	if (robotek.brojElemenata() > 0) //&& strcmp(robotek.PrintElement(indexZadnjegEl), "Over") == 0)
	{
		for (int i = 0; i <= indexZadnjegEl; i++)
		{
			char* element = robotek.PrintElement(i);

			if (strlen(element) <10)
			{
				Skreni('l', 180, brzinaKretanja);
			}
			if (strlen(element) > 10)
			{
				Skreni('d', 180, brzinaKretanja);
			}
			if (strlen(element) == 10)
			{
				Kreni(brzinaKretanja);
			}
			if (strcmp(element, "RotateLeft") == 0)
				Skreni('l', 90, brzinaKretanja);

			if (strcmp(element, "RotateRight") == 0)
				Skreni('d', 90, brzinaKretanja);

			if (strcmp(element, "RunMotors") == 0)
				Kreni(brzinaKretanja);

			if (strcmp(element, "StopMotors") == 0)
				ZaustaviMotore();

			robotek.DeleteNode(i);
		}
	}
}

void PosaljiBTPoruku(String poruka)
{
  //String prednjiSenzor = DohvatiRadnjuIzEnuma(PrednjiUZSenzor);

}

void Kreni(uint16_t brzinaKretanja)
{
  leftMotor.run(-brzinaKretanja);
  rightMotor.run(brzinaKretanja);
}

void ZaustaviMotore()
{
  leftMotor.stop();
  rightMotor.stop();
}

void Skreni(char smijer, uint16_t stupnjevi, uint16_t brzina)
{
  if (smijer == 'l')
  {
    leftMotor.run(brzina);
    rightMotor.run(brzina);
  }
  else 
  {
    leftMotor.run(-brzina);
    rightMotor.run(-brzina);
  }
  delay(IzracunajVrijemeRotacije(stupnjevi, brzina));
  ZaustaviMotore();
}

int IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
  int vrijemeOkretanje = (int)((stupnjevi * 635) / (brzina));

  return vrijemeOkretanje;
}

void exitLab() {
	
}



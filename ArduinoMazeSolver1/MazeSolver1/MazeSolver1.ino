
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"
#include "List.h"

MeBuzzer buzzer = MeBuzzer();


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
  robotek.AddNode('R');
  robotek.AddNode('O');
  robotek.AddNode('Z');
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
    //IzvrsiRadnjuBT('');
	  robotek.PrintElement(1);
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



String CitajBluetooth()
{
  String myString = "";

  if (bluetooth.available() > 0)
  {
	  while (bluetooth.read() > 0)
	  {
		  myString += bluetooth.readString();
	  }
   }

  return myString;
}

void IzvrsiRadnjuBT(char btPoruka)
{
	if (btPoruka != 'c')
	{

		if (btPoruka == 'O')
		{
			ZaustaviMotore();
		}
		if (btPoruka == 'R')
		{
			Skreni('l',90,brzinaKretanja);
		}
		else {
			Serial.write("Nema podataka");
		}


		//PosaljiBTPoruku(btPoruka);
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



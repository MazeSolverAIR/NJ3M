
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"

struct InfoZaAndroid {
	String PrednjiUZSenzor = "FUsS:";
	String DesniUZSenzor = "RUsS:";
	String LijeviUZSenzor = "LUsS:";
	String OcitajSenzorCrte = " ";
	String ZadnjiInfo = "Over:";
	String Null = "Null:";
};

struct NaredbaAndroida {
	String KreniNaprijed = "RunMotors";
	String ZaustaviSe = "StopMotors";
	String RotirajSe = "RotateFull";
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

void setup() 
{

  button.setpin(A7);

  bluetooth.begin(115200);
  bluetooth.setTimeout(25);
}


void loop() 
{
  IzvrsiPritisakTipke();

  switch (modRadnje)
  {
  case 0:
    IzvrsiRadnjuBT(CitajBluetooth());
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

void IzvrsiRadnjuBT(String btPoruka)
{
	if (btPoruka.length() > 0)
	{
		/*switch (DohvatiRadnjuIzPoruke(btPoruka))
		{
		  case KreniNaprijed:
			Kreni(brzinaKretanja);
			break;
		  case ZaustaviSe:
			ZaustaviMotore();
			break;
		  case RotirajSe:
			Skreni('l', 90, brzinaKretanja);
			break;

		  default:
			Skreni('l', 90, brzinaKretanja);
			break;
		}*/

		if (btPoruka == naredba.KreniNaprijed)
		{
			Kreni(brzinaKretanja);
		}
		else if (btPoruka == naredba.ZaustaviSe)
		{
			ZaustaviMotore();
		}


		PosaljiBTPoruku(btPoruka);
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



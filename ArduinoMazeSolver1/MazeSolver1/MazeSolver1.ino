
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"
#include "List.h"

MeBuzzer buzzer = MeBuzzer();

uint16_t brzinaKretanja = 127;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;

int index = 0;

struct objektPrimljenePoruke {
	String sadrzaj;
};

struct objektPrimljenePoruke poljeRadnji[50];


void setup()
{
	bluetooth.begin(115200);
	bluetooth.setTimeout(25);

	button.setpin(A7);
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
		  //Kreni(brzinaKretanja);
	  }
	
	CitajBluetooth();
	IzvrsiRadnjuBT();
	
    break;
  case 1:
    ZaustaviMotore();
	a = true;
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
	poljeRadnji[index].sadrzaj = bluetooth.readString();

	if (poljeRadnji[index].sadrzaj.length() > 0)
		index++;
}

void IzvrsiRadnjuBT()
{
	int chckIndex = index;

	if (chckIndex > 0)
		chckIndex--;

	if (poljeRadnji[chckIndex].sadrzaj.equals("Over"))
	{
		for (int i = 0; i < chckIndex; i++)
		{
			String radnja = poljeRadnji[i].sadrzaj;

			if (radnja.equals("RotateLeft"))
				Skreni('l', 90, brzinaKretanja);

			else if (radnja.equals("RoteteRight"))
				Skreni('d', 90, brzinaKretanja);

			else if (radnja.equals("RunMotors"))
				Kreni(brzinaKretanja);

			else if (radnja.equals("StopMotors"))
				ZaustaviMotore();

			poljeRadnji[i].sadrzaj = "";
		}

		index = 0;
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



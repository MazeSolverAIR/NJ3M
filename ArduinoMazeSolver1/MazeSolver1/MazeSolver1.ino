
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"
#include "Enumeracije.h"

uint16_t brzinaKretanja = 127;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

MeLineFollower lineFollower(2);

uint8_t modRadnje = -1;
bool stisnutGumb = true;

Enumeracije enums;

void setup() 
{
	enums = Enumeracije();

	button.setpin(A7);
	bluetooth.begin(9600);
}

void loop() 
{
	IzvrsiPritisakTipke();

	switch (modRadnje)
	{
	case 0:
		IzvrsiRadnjuBT(CitajBluetooth());
		ZaustaviMotore();
		break;
	case 1:
		IzbjegavajPrepreke();
		break;
	case 2:
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

		if (modRadnje>2)
			modRadnje = 0;
	}
	if (!button.pressed() && !stisnutGumb)
	{
		stisnutGumb = true;
	}
}

String CitajBluetooth()
{
	String returnString = "";
	if (bluetooth.available())
	{
		returnString = bluetooth.readString();
	}
	return returnString;
}

void IzvrsiRadnjuBT(String btPoruka)
{
	if (btPoruka.length() > 0)
	{
		switch (enums.DohvatiRadnjuIzPoruke(btPoruka))
		{
			case enums.KreniNaprijed:
				Kreni(brzinaKretanja);
				break;
			case enums.ZaustaviSe:
				ZaustaviMotore();
				break;
			case enums.RotirajSe:
				Skreni('l', 90, brzinaKretanja);
				ZaustaviMotore();
				break;

			default:
				Skreni('l', 90, brzinaKretanja);
				break;
		}
		PosaljiBTPoruku();
	}
}

void PosaljiBTPoruku()
{
	bluetooth.sendString("PUZS:" + ultraSonic.distanceCm);
}

void IzbjegavajPrepreke()
{
	if (ultraSonic.distanceCm() > 20)
	{
		Kreni(brzinaKretanja);
	}
	else
	{
		Skreni('l', 90, brzinaKretanja);
	}
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
}

int IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	int vrijemeOkretanje = (int)((stupnjevi * 635) / (brzina));

	return vrijemeOkretanje;
}
double lineDirIndex = 10;

void lineFollow() {
	int sensorStateCenter = lineFollower.readSensors();

	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		//senzori su na centru, kreæi se ravno
		Kreni(brzinaKretanja);
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije
		Kreni(brzinaKretanja);
		if (lineDirIndex > 1) {
			lineDirIndex--;
		}
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije
		Kreni(brzinaKretanja);
		if (lineDirIndex < 20) {
			lineDirIndex++;
		}
		break;
	case S1_OUT_S2_OUT:
		//oba senzora su van linije
		if (lineDirIndex == 10) {
			Kreni(-brzinaKretanja);
		}
		if (lineDirIndex == 10.5) {
			leftMotor.run(-brzinaKretanja);
			rightMotor.run(brzinaKretanja / 1.8);
		}
		if (lineDirIndex < 10) {
			//skreni lijevo			
		}
		if (lineDirIndex > 10.5) {
			// skreni desno
		}
		break;
	}
}
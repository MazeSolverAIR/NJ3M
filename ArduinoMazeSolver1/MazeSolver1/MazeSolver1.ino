
#include "MeMCore.h"

uint16_t brzinaKretanja = 200;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

MeBluetooth


float omjerSpeed = 0.0f;
float vrijemeOkretanjeZaDevedeset = 0.0f;

void setup() 
{
}

void loop() 
{
	Skreni('l', 90, brzinaKretanja); 
	ZaustaviMotore();

	delay(2000);

	/*if (ultraSonic.distanceCm() > 20)
	{
		Kreni(150);
	}
	else
	{
		ZaustaviMotore();
		delay(1000);
		
	}*/
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

float IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	float vrijemeOkretanje = (((float)stupnjevi * 676) / (float)brzina)+(360/stupnjevi);

	return vrijemeOkretanje;
}

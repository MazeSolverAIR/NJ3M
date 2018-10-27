
#include "MeMCore.h"

uint16_t brzinaKretanja = 150;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);


float omjerSpeed = 0.0f;
float vrijemeOkretanjeZaDevedeset = 0.0f;

void setup() 
{
	omjerSpeed = (float)90 / (float)472;
	vrijemeOkretanjeZaDevedeset = IzracunajVrijemeRotacije(90, brzinaKretanja);
}

void loop() 
{

	if (ultraSonic.distanceCm() > 20)
	{
		Kreni(150);
	}
	else
	{
		ZaustaviMotore();
		delay(1000);
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
	delay(vrijemeOkretanjeZaDevedeset);
}

float IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	float vrijemeOkretanje = (float)stupnjevi / (omjerSpeed*((float)brzina / (float)127));

	return vrijemeOkretanje;
}

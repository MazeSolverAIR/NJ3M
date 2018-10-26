#include "Motori.h"


MeDCMotor leftMotor;
MeDCMotor rightMotor;




Motori::Motori(uint8_t pinRight, uint8_t pinLeft)
{
	leftMotor = MeDCMotor(pinLeft);
	rightMotor = MeDCMotor(pinRight);
}

void Motori:: KreniNaprijed(uint16_t brzinaKretanja)
{
	leftMotor.run(-brzinaKretanja);
	rightMotor.run(brzinaKretanja);
}

void Motori:: Zaustavi()
{
	leftMotor.stop();
	rightMotor.stop();
}

void Motori:: Skreni(char smijer, int stupnjevi, uint16_t motorSpeed)
{
	float vrijemeTrajanjaRotacije = IzracunajVrijemeRotacije(stupnjevi, motorSpeed);
	if (smijer == 'l')
	{
		leftMotor.run(motorSpeed);
		rightMotor.run(motorSpeed);
	}
	else
	{
		rightMotor.run(-motorSpeed);
		leftMotor.run(-motorSpeed);
	}

	delay((int)vrijemeTrajanjaRotacije);
}

float Motori::IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t motorSpeed)
{
	float vrijeme = 0;
	float omjerConst = 90 / 255;

	return vrijeme = stupnjevi / (((float)motorSpeed / 255)*omjerConst);
}

Motori::~Motori()
{

}



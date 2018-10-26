#include "Motors.h"

Motors::Motors()
{
	MeDCMotor rightMotor(M2);
	MeDCMotor leftMotor(M1);
}

Motors::~Motors()
{
}

void Motors::KreniNaprijed(uint16_t brzinaKretanja)
{
	leftMotor.run(-brzinaKretanja);
	rightMotor.run(brzinaKretanja);
}

void Motors::Zaustavi()
{
	leftMotor.stop();
	rightMotor.stop();
}

void Motors::Skreni(char smijer, uint16_t stupnjevi, uint16_t motorSpeed)
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

float Motors::IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t motorSpeed)
{
	float vrijeme = 0;
	float omjerConst = 90 / 255;

	return vrijeme = stupnjevi / (((float)motorSpeed / 255)*omjerConst);
}

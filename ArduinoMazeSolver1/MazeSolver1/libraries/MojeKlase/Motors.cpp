#include "Motors.h"

Motors::Motors()
{
	MeDCMotor leftMotor(M1);
	MeDCMotor rightMotor(M2);
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

void Motors::Skreni(char, uint16_t, uint16_t)
{
	//Nesto

}

float Motors::IzracunajVrijemeRotacije(uint16_t, uint16_t)
{
	return 0.0f;
}

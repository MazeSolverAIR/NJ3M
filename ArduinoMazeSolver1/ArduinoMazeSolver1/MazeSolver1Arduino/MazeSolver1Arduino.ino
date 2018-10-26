#include "Motors.h"


void setup()
{

}

void loop()
{
	MeUltrasonicSensor senzor;

	Motors motors;

	if (senzor.distanceCm() > 15)
		motors.KreniNaprijed(150);

	else
		motors.Skreni('d', 90, 150);
}




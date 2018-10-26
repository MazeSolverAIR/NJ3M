#include "Motors.h"

MeUltrasonicSensor senzor;

Motors motors;

void setup()
{
	senzor = MeUltrasonicSensor(3);
	motors = Motors();
}

void loop()
{

	if (senzor.distanceCm() > 15)
		motors.KreniNaprijed(150);

	else
		motors.Skreni('d', 90, 150);
}




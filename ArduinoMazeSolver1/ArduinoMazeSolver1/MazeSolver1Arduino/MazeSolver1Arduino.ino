#include "Motori.h"


MeUltrasonicSensor senzor;

Motori motors(M1, M2);

void setup()
{
	senzor = MeUltrasonicSensor(3);

}

void loop()
{
	if (senzor.distanceCm() > 15)
		motors.KreniNaprijed(150);

	else
		motors.Skreni('d', 90, 150);

}




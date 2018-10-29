
#include "MeMCore.h"
#include "Arduino.h"

uint16_t brzinaKretanja = 100;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

uint8_t modRadnje = -1;
bool stisnutGumb = true;

int state = 0;

void setup() 
{
	button.setpin(A7);

	Serial.begin(38400);
}

void loop() 
{

	if (button.pressed() && stisnutGumb)
	{
		stisnutGumb = false;
		modRadnje++;

		if (modRadnje > 2)
			modRadnje = 0;
	}
	if (!button.pressed())
	{
		stisnutGumb = true;
	}

	switch (modRadnje)
	{
	case 0:
		IzbjegavajPrepreke();
		break;
	case 1:
		UpaliBlueTooth();
		break;
	case 2:
		ZaustaviMotore();
	default:	
		ZaustaviMotore();
		break;
	}

}

void UpaliBlueTooth()
{
	if (Serial.available() > 0) {
		state = Serial.read();
	}

	if (state == '1') {
		Kreni(brzinaKretanja);
		state = 0;
	}
}

void IzbjegavajPrepreke()
{
	if (ultraSonic.distanceCm() > 20)
	{
		Kreni(150);
	}
	else
	{
		ZaustaviMotore();
		delay(1000);

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

float IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	float vrijemeOkretanje = (((float)stupnjevi * 676) / (float)brzina)+(360/stupnjevi);

	return vrijemeOkretanje;
}

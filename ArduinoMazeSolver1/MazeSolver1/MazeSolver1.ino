
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"

uint16_t brzinaKretanja = 127;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

MeBluetooth bluetooth(PORT_3);

uint8_t modRadnje = -1;
bool stisnutGumb = true;

int readdata = 0, i = 0, count = 0;
char outDat;

unsigned char table[128] = { 0 };

void setup() 
{
	button.setpin(A7);

	Serial.begin(115200);
	bluetooth.begin(115200);    //The factory default baud rate is 115200
	Serial.println("Bluetooth Start!");
	
}

void loop() 
{
	IzvrsiRadnjuMijenjanjaModa();

	switch (modRadnje)
	{
	case 0:
		IzbjegavajPrepreke();
		break;
	case 1:
		ZaustaviMotore();
		UpaliBlueTooth();
		break;
	case 2:
		ZaustaviMotore();
	default:	
		ZaustaviMotore();
		break;
	}

}

void IzvrsiRadnjuMijenjanjaModa()
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

void UpaliBlueTooth()
{
	if (bluetooth.available())
	{
		if (bluetooth.readString().length() > 0)
		{
			Skreni('l', 90, brzinaKretanja);
		}
	}
	/*if (Serial.available())
	{
		outDat = Serial.read();
		bluetooth.write(outDat);
	}*/
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

float IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	float vrijemeOkretanje = (((float)stupnjevi * 676) / (float)brzina)+(360/stupnjevi);

	return vrijemeOkretanje;
}

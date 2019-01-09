#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"
#include "List.h"

uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;
bool b = true;

int index = 0;

struct objektPrimljenePoruke {
	String sadrzaj;
};

struct objektPrimljenePoruke poljeRadnji[50] = {};

MeBuzzer buzzer = MeBuzzer();
String myString;
MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);
MeUltrasonicSensor ultraSonic(3);
//MeUltrasonicSensor ultraSonicLeft(4);

uint16_t brzinaKretanja = 127;


Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

MeLineFollower lineFollower(2);

void setup()
{

	button.setpin(A7);

	bluetooth.begin(115200);
	bluetooth.setTimeout(10);

	//Kreiranje liste
}

void loop()
{
	IzvrsiPritisakTipke();

	switch (modRadnje)
	{
	case 0:
		if (a) {
			//buzzer.tone(700, 500);
			a = false;
		}
		CitajBluetooth();
		IzvrsiRadnjuBT();

		//lineFollow();
		b = true;
		break;
	case 1:

		if (b) {
			buzzer.tone(700, 500);
			Serial.write("KreceWrite;");

			b = false;
		}
	
		/*Serial.println(ultraSonicLeft.distanceCm());
		if (ultraSonicLeft.distanceCm() < 10)
			Kreni(brzinaKretanja);

		else
			ZaustaviMotore();
			*/
	//	delay(5);

		a = true;
		break;

	default:
		ZaustaviMotore();
		break;
	}
}


void IzvrsiPritisakTipke()
{
	if (button.pressed() && stisnutGumb)
	{
		stisnutGumb = false;
		modRadnje++;

		if (modRadnje > 1)
			modRadnje = 0;
	}
	if (!button.pressed() && !stisnutGumb)
	{
		stisnutGumb = true;
	}
}


void CitajBluetooth()
{
	String btPoruka = bluetooth.readString();

	if (btPoruka.startsWith("MS:"))
	{
		String gotovaPoruka = btPoruka.substring(btPoruka.lastIndexOf(':')+1, btPoruka.length());

		poljeRadnji[index].sadrzaj = gotovaPoruka;
		index++;
	}
}

void IzvrsiRadnjuBT()
{
	int chckIndex = index;

	if (chckIndex > 0)
		chckIndex--;

	if (poljeRadnji[chckIndex].sadrzaj.equals("Over"))
	{
		for (int i = 0; i < chckIndex; i++)
		{
			String radnja = poljeRadnji[i].sadrzaj;

			if (radnja.equals("RotateLeft"))
				Skreni('l', 90, brzinaKretanja);

			else if (radnja.equals("RotateRight"))
				Skreni('d', 90, brzinaKretanja);

			else if (radnja.equals("RunMotors"))
				Kreni(brzinaKretanja);

			else if (radnja.equals("StopMotors"))
				ZaustaviMotore();

			else if (radnja.equals("SpeedUpLeft")) {
				leftMotor.run(-137);
				rightMotor.run(brzinaKretanja);
			}
			else if (radnja.equals("SpeedUpRight")) {
				rightMotor.run(137);
				leftMotor.run(-brzinaKretanja);
			}
		}
		poljeRadnji[50] = {};

		index = 0;
	}
		

}


void PosaljiBTPoruku()
{
	/*FUS:vrijednost; LUS:vrijednost; RUS:vrijednost; LFL:vrijednost; LFR:vrijednost;
	 FUS - FrontUltrasonicSensor  LUS - LeftUltrasonicSensor  RUS - RightUltrasonicSensor
	 LFL - LineFollowerLeft  LFR - LineFolovwerRight
	 Šalje se informacija po informacija, te na kraju "Over"*/
	bluetooth.print("Hello");
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
	ZaustaviMotore();
}

int IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	int vrijemeOkretanje = (int)((stupnjevi * 635) / (brzina));

	return vrijemeOkretanje;
}

void exitLab() {

}

void lineFollow() {
	int sensorStateCenter = lineFollower.readSensors();

	//provjeriti slanje očitanja senzora mobilnoj aplikaciji

	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		//senzori su na centru, kre�i se ravno
		bluetooth.print("OnLine");
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		bluetooth.print("RightOut");
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		bluetooth.print("LeftOut");
		break;
	case S1_OUT_S2_OUT:
		//oba senzora su van linije
		bluetooth.print("BothOut");
		break;
	}
}

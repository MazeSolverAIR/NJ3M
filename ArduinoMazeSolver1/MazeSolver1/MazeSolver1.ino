﻿#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"

uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;
bool b = true;

int index = -1;

typedef struct objektPrimljenePoruke {
	String sadrzaj;
};

const int velicinaPolja = 10;
objektPrimljenePoruke poljeRadnji[velicinaPolja] = {""};

MeBuzzer buzzer = MeBuzzer();
String myString;
MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);
MeUltrasonicSensor ultraSonic(3);
MeUltrasonicSensor ultraSonicLeft(4);

uint16_t brzinaKretanja = 127;


Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

MeLineFollower lineFollower(2);

void setup()
{

	button.setpin(A7);

	/*bluetooth.begin(115200);
	bluetooth.setTimeout(2);*/

	Serial.begin(115200);
	Serial.setTimeout(2);
}

void loop()
{
	IzvrsiPritisakTipke();

	switch (modRadnje)
	{
	case 0:
		if (a) {
			buzzer.tone(700, 500);
			a = false;
		}
		/*if(CitajBluetooth().equals("Over"))
		{
			IzvrsiRadnjuBT();
			inicijalizirajPolje();
		}*/
			

		//lineFollow();
		b = true;
		break;
	case 1:

		if (b) {
			buzzer.tone(700, 500);
			Serial.write("KreceWrite;");

			b = false;
		}
	
		Serial.println((ultraSonicLeft.aRead1() / 1.833)*2.54); //pretvaranje u cm
		
		delay(5);

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


String CitajBluetooth()
{
	//String btPoruka = Serial.readString();
	String btPoruka = bluetooth.readString();

	if (btPoruka.startsWith("MS:"))
	{
		index++;
		String gotovaPoruka = btPoruka.substring(btPoruka.lastIndexOf(':') + 1, btPoruka.length());

		poljeRadnji[index].sadrzaj = gotovaPoruka;

		/*Serial.print("Poruka: ");
		Serial.println(gotovaPoruka);
		Serial.print(" / ");
		Serial.println(poljeRadnji[index].sadrzaj);*/

		return gotovaPoruka;
	}

	return "";
}

void IzvrsiRadnjuBT()
{
	int chckIndex = index-1;

	//Serial.println("Provjera radnji: ");
	for (int i = 0; i <= chckIndex; i++)
	{
		String radnja = poljeRadnji[i].sadrzaj;

		/*Serial.print("Elelement pod rednim brojem: ");
		Serial.print(i);
		Serial.print(" je :");
		Serial.println(radnja);*/
		if (radnja.equals("RotateLeft"))
			Skreni('l', 90, brzinaKretanja);

		else if (radnja.equals("RotateRight"))
			Skreni('d', 90, brzinaKretanja);

		else if (radnja.equals("RunMotors"))
			Kreni(brzinaKretanja);

		else if (radnja.equals("StopMotors"))
			ZaustaviMotore();

		else if (radnja.equals("SpeedUpLeft")) 
		{
			leftMotor.run(-137);
			rightMotor.run(brzinaKretanja);
		}
		else if (radnja.equals("SpeedUpRight")) 
		{
			rightMotor.run(137);
			leftMotor.run(-brzinaKretanja);
		}

		delay(5);
	}
}

void inicijalizirajPolje()
{
	//Serial.println("Brisanje radnji: ");
	for (int i = 0; i < velicinaPolja; i++)
	{
		//Serial.println(poljeRadnji[i].sadrzaj);
		poljeRadnji[i] = {};
		//Serial.println(poljeRadnji[i].sadrzaj);
	}

	index = -1;
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

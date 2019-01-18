#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"


uint8_t modRadnje = -1;
bool stisnutGumb = true;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(1);
MeUltrasonicSensor ultraSonicRight(4);
MeUltrasonicSensor ultraSonicLeft(3);

MeLineFollower lineFollower(2);

uint16_t brzinaKretanja = 80;


Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();
bool poslano = false;


double detektirajZid;
double udaljenostDoZida;
double doZidaMax;
double trajanje;
unsigned long skrenuoDesno=0;
char previseOkretaja;


void setup()
{
	button.setpin(A7);

	Serial.begin(115200);
	//Serial.setTimeout(1);
}

//String poruka = "";
void loop()
{
	rjesenjeProblemaDesno();
	/*while (!(lineFollower.readSensors() == 3)) {
		Skreni('l', 90, brzinaKretanja);
		pronadjiZid();
		if (detektirajZid == 1) {
			Skreni('d', 90, brzinaKretanja);
			pronadjiZid();
			if (detektirajZid == 1) {
				Skreni('d', 90, brzinaKretanja);
				pronadjiZid();
				if (detektirajZid == 1) {
					Skreni('d', 90, brzinaKretanja);
					pronadjiZid();
				}
				else {
					Kreni(brzinaKretanja);
				}
			}
			else {
				Kreni(brzinaKretanja);
			}
		}
		else {
			Kreni(brzinaKretanja);
		}
	}*/

	/*if (ultraSonic.distanceCm() < 20) {
		ZaustaviMotore();
		if (poslano == false) {
			Serial.println(lineFollower.readSensors());
			poruka = "";
			poruka += "c";
			poruka += ultraSonic.distanceCm();
			poruka += "m";
			Serial.println(poruka);
		}
		poslano = true;
	}
	else {
		rjesenjeProblema();
		Kreni(brzinaKretanja);
		poslano = false;
	}
	delay(5);
	if (poslano == true) {
		String hehe = Serial.readString();
		if (hehe == "RL")
		{
			Skreni('l', 90, brzinaKretanja);
			poslano = false;
		}

		if (hehe == "RR")
		{
			Skreni('d', 90, brzinaKretanja);
			poslano = false;
		}

		if (hehe == "RM")
		{
			Kreni(brzinaKretanja);
			poslano = false;
		}

		if (hehe == "SM")
		{
			ZaustaviMotore();
			poslano = false;
		}

		if (hehe == "SUR")
		{
			rightMotor.run(brzinaKretanja + 25);
			leftMotor.run(-brzinaKretanja + 25);
			poslano = false;
		}

		if (hehe == "SUL")
		{
			leftMotor.run(-brzinaKretanja - 25);
			rightMotor.run(brzinaKretanja - 25);
			poslano = false;
		}

		if (hehe == "RF")
		{
			Skreni('l', 90, 500);
			Skreni('l', 90, 500);
			poslano = false;
		}

	}
	//delay(5);
	*/
}
	
	


/*long vrijemeOdZadnjePoruke()
{
	return abs(vrijemePrimanjaPoruke-vrijemePocetkaPetlje);
}

float GetFrontSensorDistance()
{
	long travelTime = ultraSonic.measure();

	return (travelTime / 2) / 29.1;
}

float GetSideSensorDistance(uint16_t analogData)
{
	return (analogData / 1.75) * 2.54;
}*/

void IzvrsiPritisakTipke()
{
	if (button.pressed() && stisnutGumb)
	{
		stisnutGumb = false;
		modRadnje++;

		if (modRadnje > 2)
			modRadnje = 0;
	}
	if (!button.pressed() && !stisnutGumb)
	{
		stisnutGumb = true;
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
		leftMotor.run(150);
		rightMotor.run(150);
	}
	else
	{
		leftMotor.run(-150);
		rightMotor.run(-150);
	}
	delay(IzracunajVrijemeRotacije(stupnjevi,brzina));
	ZaustaviMotore();
}

unsigned long IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	unsigned long vrijemeOkretanje = (stupnjevi * 650) / (brzina);

	return vrijemeOkretanje;
}

String lineFollow() {
	int sensorStateCenter = lineFollower.readSensors();

	//provjeriti slanje očitanja senzora mobilnoj aplikaciji

	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		//senzori su na centru, kre�i se ravno
		return "OnLine";
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		return "RightOut";
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		return "LeftOut";
		break;
	case S1_OUT_S2_OUT:
		//oba senzora su van linije
		return "BothOut";
		break;
	}
}

void rjesenjeProblemaDesno() {
	unsigned long sensorStateCenter = lineFollower.readSensors();
	unsigned long udaljenostDoZida = ultraSonic.distanceCm();
	//provjeriti slanje očitanja senzora mobilnoj aplikaciji
	//Serial.println(sensorStateCenter);
	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		if (udaljenostDoZida <= 22) {
			Skreni('d', 66, brzinaKretanja);
			delay(1300);
			unsigned long novaDistanca = ultraSonic.distanceCm();
			if (novaDistanca <= 22) {
				Skreni('d', 66, brzinaKretanja);
				delay(1300);
				//ponovno skreni da se ne vracas unatrag
				if (ultraSonic.distanceCm() > 22) {
					Skreni('d', 66, brzinaKretanja);
					delay(1300);
					if (ultraSonic.distanceCm() <= 22) {
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
					}
					else {

					}
				}
				else {

				}
			}
			else {

			}
		}
		else {
			Kreni(brzinaKretanja);
		}

		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		ZaustaviMotore();
		Skreni('l', 5, 40);
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		ZaustaviMotore();
		Skreni('d', 5, 40);
		break;
	case S1_OUT_S2_OUT:
		Skreni('d', 5, 40);
		break;
	}
}

void rjesenjeProblema() {
	unsigned long sensorStateCenter = lineFollower.readSensors();
	unsigned long udaljenostDoZida = ultraSonic.distanceCm();
	//provjeriti slanje očitanja senzora mobilnoj aplikaciji
	//Serial.println(sensorStateCenter);
	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		if (udaljenostDoZida <= 22) {
			Skreni('l', 75, brzinaKretanja);
			delay(1300);
			if (ultraSonic.distanceCm() <= 22) {
				Skreni('l', 75, brzinaKretanja);
			}
			else {

			}
		}
		else {
			Kreni(brzinaKretanja);
		}
		
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		ZaustaviMotore();
		Skreni('l', 5, 40);
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		ZaustaviMotore();
		Skreni('d', 5, 40);
		break;
	case S1_OUT_S2_OUT:
		Skreni('l', 5, 40);
		break;
	}
}

void pronadjiCrtu() {
	do {
		Skreni('l', 5, brzinaKretanja);
	} while (S1_OUT_S2_OUT);
}

void pronadjiZid() {
	detektirajZid = 0;
	delay(0.2);
	udaljenostDoZida = ultraSonic.distanceCm();
	trajanje += 0.2;
	if ((udaljenostDoZida) < (doZidaMax)) {
		detektirajZid = 1;
	}
	
}

void rjesenjeProblemaGlavnaFora() {

	//Serial.println(haba);

		//Serial.println(Serial.available());
}




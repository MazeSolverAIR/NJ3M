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
	Serial.setTimeout(5);
}

void loop()
{
	rjesenjeProblemaAutonomno();
}

String saljiPodatkeMobitelu() {
	String porukaMobitelu = "";
	porukaMobitelu += ultraSonic.distanceCm();
	porukaMobitelu += "b";
	return porukaMobitelu;
}

//TO-DO Napraviti metodu koja čita bluetooth string za ponavljanje zadnje naredbe

void IzvrsiNaredbu(String poruka) {
	if (poruka.equals("RM")) {
		Kreni(brzinaKretanja);
	}
	else if (poruka.equals("SM")) {
		ZaustaviMotore();
	}
	else if (poruka.equals("SL")) 
	{
		ZaustaviMotore();
		Skreni('l', 66, brzinaKretanja);
		Serial.println("OK");
		delay(1500);
	}
	else if (poruka.equals("SD")) {
		ZaustaviMotore();
		Skreni('d', 66, brzinaKretanja);
		delay(1500);
	}
	else if (poruka.equals("FR")) {
		ZaustaviMotore();
		Skreni('l', 150, brzinaKretanja);
		Skreni('l', 150, brzinaKretanja);
		delay(1500);
	}
}

//TO-DO Ako se izvrši neki case da nije na liniji, zapamtiti da se nije zadnja radnja izvršila

bool stop = true;
void rjesenjeProblemaAutonomno() {
	String poruka = "";
	poruka = Serial.readString();

	if (poruka.equals("STP"))
	{
		stop = true;
		return;
	}
	else if (poruka.equals("RUN"))
		stop = false;

	if (!stop)
	{
		Serial.println(saljiPodatkeMobitelu());
		delay(10);
		unsigned long sensorStateCenter = lineFollower.readSensors();
		//provjeriti slanje očitanja senzora mobilnoj aplikaciji
		//Serial.println(sensorStateCenter);
		switch (sensorStateCenter)
		{
		case S1_IN_S2_IN:
			IzvrsiNaredbu(poruka);
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
}

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






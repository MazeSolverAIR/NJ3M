#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"

uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;
bool b = true;

int index = -1;

typedef struct objektPrimljenePoruke {
	String sadrzaj;
	int brojPoruka;
};

const int velicinaPolja = 10;
objektPrimljenePoruke poljeRadnji[velicinaPolja];

MeBuzzer buzzer = MeBuzzer();
String myString;
MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);
MeUltrasonicSensor ultraSonicRight(4);

MeLineFollower lineFollower(1);

uint16_t brzinaKretanja = 127;


Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

void setup()
{

	button.setpin(A7);

	bluetooth.begin(115200);
	bluetooth.setTimeout(2);

	/*Serial.begin(9600);
	Serial.setTimeout(2);*/
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
		if(CitajBluetooth().equals("Over"))
		{
			if (!ProvjeriBrojPrimljenihPoruka)
				PosaljiZahtjevZaPonovnimSlanjem();

			else 
				IzvrsiRadnjuBT();


			inicijalizirajPolje();
		}
		else if (CitajBluetooth().equals("NeispravnaPoruka"))
		{
			PosaljiZahtjevZaPonovnimSlanjem();
		}


		//lineFollow();
		b = true;
		break;
	case 1:

		if (b) {
			buzzer.tone(700, 500);
			Serial.write("KreceWrite;");

			b = false;
		}

		
		Serial.print("Right: ");
		Serial.println((ultraSonicRight.aRead1() / 1.7) * 2.54);
		delay(25);
		/*Serial.print("Front: ");
		Serial.println((ultraSonic.distanceCm()));
		delay(25);

		Serial.println(analogRead(1));*/

		//Kreni(brzinaKretanja);
		/*if (ultraSonic.distanceCm() < 25)
		{
			Skreni('l', 90, brzinaKretanja);
			Skreni('l', 90, brzinaKretanja);
		}	
		else if (((ultraSonicRight.aRead1() / 1.75)*2.54) < 23)
			Skreni('l', 90, brzinaKretanja);

		else if ((ultraSonicLeft.aRead1() *1.5) < 23)
			Skreni('d', 90, brzinaKretanja);

		Kreni(brzinaKretanja);*/
			

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

	if (btPoruka.startsWith("MS:") /*&& asciiSumaDobivenePoruke==asciiSumaIzracunato && istePoruke*/)
	{
		index++;

		String cijelaPoruka = btPoruka.substring(btPoruka.lastIndexOf(':') + 1);
		String porukaBezBrojaPoruka = cijelaPoruka.substring(0, cijelaPoruka.indexOf('#'));
		int ocekivaniBrojPoruka = cijelaPoruka.substring(cijelaPoruka.lastIndexOf('#') + 1).toInt();

		String dobivenaPoruka = cijelaPoruka.substring(0, cijelaPoruka.lastIndexOf(';'));
		int asciiSumaIzracunato = IzracunajASciiSumu(dobivenaPoruka);
		int asciiSumaDobivenePoruke = porukaBezBrojaPoruka.substring(porukaBezBrojaPoruka.lastIndexOf(';') + 1).toInt();

		poljeRadnji[index].sadrzaj = dobivenaPoruka;
		poljeRadnji[index].brojPoruka = ocekivaniBrojPoruka;

		if(asciiSumaIzracunato != asciiSumaDobivenePoruke)
			return "NeispravnaPoruka";

		/*Serial.print("Poruka: ");
		Serial.println(gotovaPoruka);
		Serial.print(" / ");
		Serial.println(poljeRadnji[index].sadrzaj);*/

		return dobivenaPoruka;
	}

	return btPoruka;
}

void PosaljiZahtjevZaPonovnimSlanjem()
{
	//TODO: Napraviti metodu koja šalje zahtjev za ponovnim slanjem istih poruka jer se je dogodila greška
}

bool ProvjeriBrojPrimljenihPoruka() {
	for (int i = 0; i <= index; i++)
	{
		for (int j = 1; j <= index; j++)
		{
			if (poljeRadnji[i].brojPoruka != poljeRadnji[j].brojPoruka)
			{
				return false;
			}
		}
	}
	return true;
}

int IzracunajASciiSumu(String rijec) {
	int output=0;

	for (int i = 0; i < rijec.length(); i++)
	{
		output += (int)rijec.charAt(i);
	}
	
	return output;
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

#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"


uint8_t modRadnje = -1;
bool stisnutGumb = true;
bool a = true;
bool b = true;

int index = -1;
int indexZaSlanje = -1;
bool neispravnaPorukaPrimljena = false;

long vrijemePrimanjaPoruke = 0;
long vrijemePocetkaPetlje = 0;

typedef struct objektPrimljenePoruke {
	String sadrzaj;
	int brojPoruka;
};

const int velicinaPolja = 9;

objektPrimljenePoruke poljeRadnji[velicinaPolja] = {};
String metodeZaSlanje[velicinaPolja] = {};

MeBuzzer buzzer = MeBuzzer();
String myString;
MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(1);
MeUltrasonicSensor ultraSonicRight(4);
MeUltrasonicSensor ultraSonicLeft(3);

MeLineFollower lineFollower(2);

uint16_t brzinaKretanja = 127;


Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

void setup()
{
	button.setpin(A7);

	bluetooth.begin(115200);
	bluetooth.setTimeout(1);

	Serial.setTimeout(1);
}

void loop()
{
	IzvrsiPritisakTipke();
	String procitanaBTPoruka = "";
	switch (modRadnje)
	{
	case 0:
		if (a) {
			buzzer.tone(700, 500);
			a = false;
		}
		vrijemePocetkaPetlje = millis();
		procitanaBTPoruka = CitajBluetooth();

		if (procitanaBTPoruka.equals("Over") && !neispravnaPorukaPrimljena)
		{
			if (ProvjeriBrojPrimljenihPoruka())
			{
				IzvrsiRadnjuBT();
				PosaljiInfoSenzora();
			}

			else
				PosaljiZahtjevZaPonovnimSlanjem();

			InicijalizirajPoljePrimljenihRadnji();
		}
		else if (neispravnaPorukaPrimljena && vrijemeOdZadnjePoruke() > 100)
		{
			PosaljiZahtjevZaPonovnimSlanjem();
			neispravnaPorukaPrimljena = false;

			InicijalizirajPoljePrimljenihRadnji();
		}


		//lineFollow();
		b = true;
		break;
	case 1:

		if (b) {
			buzzer.tone(1800, 500);

			b = false;
			delay(1000);

		}

		a = true;
		break;

	default:
		ZaustaviMotore();
		break;
	}
}


long vrijemeOdZadnjePoruke()
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
	String btPoruka = "";
	if (bluetooth.available() > 0)
	{
		btPoruka = bluetooth.readString();

		if (btPoruka.startsWith("MS:"))
		{
			index++;

			String cijelaPoruka = btPoruka.substring(btPoruka.lastIndexOf(':') + 1); //Poruka nakon MS:

			ZapisiPorukuUPolje(cijelaPoruka);

			if (!CheckAsciiSuma(cijelaPoruka))
				neispravnaPorukaPrimljena = true;

			vrijemePrimanjaPoruke = millis();

			return DohvatiTekstPoruke(cijelaPoruka);
		}
	}	

	return btPoruka;
}

void ZapisiPorukuUPolje(String cijelaPoruka)
{
	int ocekivaniBrojPoruka = DohvatiOcekivaniBrojPoruka(cijelaPoruka);
	String dobivenaPoruka = DohvatiTekstPoruke(cijelaPoruka);

	poljeRadnji[index].sadrzaj = dobivenaPoruka;
	poljeRadnji[index].brojPoruka = ocekivaniBrojPoruka;
}



String DohvatiTekstPoruke(String cijelaPoruka)
{
	return cijelaPoruka.substring(0, cijelaPoruka.lastIndexOf(';'));
}

bool CheckAsciiSuma(String cijelaPoruka)
{
	int asciiSumaIzracunato = IzracunajASciiSumu(DohvatiTekstPoruke(cijelaPoruka));
	int asciiSumaDobivenaIzPoruke = DohvatiAsciiSumuIzPoruke(cijelaPoruka);

	return asciiSumaIzracunato == asciiSumaDobivenaIzPoruke;
}

int DohvatiAsciiSumuIzPoruke(String cijelaPoruka)
{
	String porukaBezBrojaPoruka = cijelaPoruka.substring(0, cijelaPoruka.indexOf('#'));

	return porukaBezBrojaPoruka.substring(porukaBezBrojaPoruka.lastIndexOf(';') + 1).toInt();
}

String SpojiPoruku(String poruka) {

	int asciiSuma = IzracunajASciiSumu(poruka);
	int ocekivaniBrojPoruka = indexZaSlanje + 1;

	return "MBot:" + poruka + ';' + asciiSuma + '#' + ocekivaniBrojPoruka + "&";
}

void SaljiPoruke() {
	for (int i = 0; i <= indexZaSlanje; i++) {
		String porukaZaSlanje = SpojiPoruku(metodeZaSlanje[i]);

		for (int j=0;j<porukaZaSlanje.length();j++)
		{
			Serial.print(porukaZaSlanje[j]);
		}
		delay(20);
	}
}

void InicijalizirajMetodeZaSlanje()
{
	for (int i=0;i<velicinaPolja;i++)
		metodeZaSlanje[i] = { "" };

	indexZaSlanje = -1;
}

void ZapisiNaredbuUPolje(String poruka) {
	indexZaSlanje++;
	metodeZaSlanje[indexZaSlanje] = poruka;
}

uint8_t DohvatiOcekivaniBrojPoruka(String cijelaPoruka)
{
	return cijelaPoruka.substring(cijelaPoruka.lastIndexOf('#') + 1).toInt();
}

void PosaljiInfoSenzora()
{

	InicijalizirajMetodeZaSlanje();

	String FUS = "FUS'";
	FUS += ultraSonic.distanceCm();
	ZapisiNaredbuUPolje(FUS);

	String LUS = "LUS'";
	LUS += GetSideSensorDistance(ultraSonicLeft.aRead1());
	ZapisiNaredbuUPolje(LUS);

	String RUS = "RUS'";
	RUS += GetSideSensorDistance(ultraSonicRight.aRead1());
	ZapisiNaredbuUPolje(RUS);

	ZapisiNaredbuUPolje("Over");

	SaljiPoruke();
}

void PosaljiZahtjevZaPonovnimSlanjem()
{
	InicijalizirajMetodeZaSlanje();

	ZapisiNaredbuUPolje("SendAgain");
	ZapisiNaredbuUPolje("Over");

	SaljiPoruke();
}

void PonovnoPosaljiPoruke()
{
	SaljiPoruke();
}

bool ProvjeriBrojPrimljenihPoruka() {
	for (int i = 0; i <= index; i++)
	{
		if (poljeRadnji[i].brojPoruka != index + 1)
			return false;
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
	int output = 0;

	for (int i = 0; i < rijec.length(); i++)
	{
		output += (int)rijec.charAt(i);
	}

	return output;
}



void IzvrsiRadnjuBT()
{
	int chckIndex = index - 1;

	for (int i = 0; i <= chckIndex; i++)
	{
		String radnja = poljeRadnji[i].sadrzaj;

		/*if (radnja.equals("SendAgain"))
		{
			PosaljiInfoSenzora();
		}
		if (!radnja.equals("SendAgain"))
		{
			InicijalizirajMetodeZaSlanje();
			poljeInfoJeInicijalizirano = true;
		}*/

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
			leftMotor.run(-brzinaKretanja-15);
			rightMotor.run(brzinaKretanja);
			delay(250);
		}
		else if (radnja.equals("SpeedUpRight"))
		{
			rightMotor.run(brzinaKretanja + 15);
			leftMotor.run(-brzinaKretanja);
			delay(250);
		}
	}
}

void InicijalizirajPoljePrimljenihRadnji()
{
	for (int i = 0; i < velicinaPolja; i ++ )
	{
		poljeRadnji[i].sadrzaj = { "" };
		poljeRadnji[i].brojPoruka = { 0 };
	}
	

	index = -1;
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

/*void lineFollow() {
	int sensorStateCenter = lineFollower.readSensors();

	//provjeriti slanje očitanja senzora mobilnoj aplikaciji

	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		//senzori su na centru, kre�i se ravno
		Serial.print("OnLine");
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		Serial.print("RightOut");
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		Serial.print("LeftOut");
		break;
	case S1_OUT_S2_OUT:
		//oba senzora su van linije
		Serial.print("BothOut");
		break;
	}
}*/
